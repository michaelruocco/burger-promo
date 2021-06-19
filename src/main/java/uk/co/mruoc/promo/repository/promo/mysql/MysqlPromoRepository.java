package uk.co.mruoc.promo.repository.promo.mysql;

import com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.promo.entity.promo.Promo;
import uk.co.mruoc.promo.entity.promo.PromoAvailability;
import uk.co.mruoc.promo.entity.promo.PromoClaimRequest;
import uk.co.mruoc.promo.usecase.UnexpectedErrorException;
import uk.co.mruoc.promo.usecase.promo.PromoRepository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
public class MysqlPromoRepository implements PromoRepository {

    private final DataSource dataSource;
    private final TimeLimiter timeLimiter;
    private final Retry retry;
    private final Executor executor = Executors.newFixedThreadPool(50);

    public MysqlPromoRepository(DataSource dataSource) {
        this(dataSource, buildTimeLimiter(), buildRetry());
    }

    @Override
    public Optional<PromoAvailability> findAvailability(PromoClaimRequest request) {
        try (var connection = dataSource.getConnection()) {
            try (var statement = connection.prepareStatement("CALL get_promo_availability(?, ?)")) {
                statement.setString(1, request.getPromoId());
                statement.setString(2, request.getAccountId());
                try (var resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(toPromoAvailability(request, resultSet));
                    }
                    return find(request.getPromoId())
                            .map(promo -> toPromoAvailability(promo, request.getAccountId()));
                }
            }
        } catch (SQLException e) {
            throw new UnexpectedErrorException(e);
        }
    }

    @Override
    public Optional<Promo> find(String promoId) {
        try (var connection = dataSource.getConnection()) {
            try (var statement = connection.prepareStatement("CALL get_promo(?);")) {
                statement.setString(1, promoId);
                try (var resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(toPromo(promoId, resultSet));
                    }
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new UnexpectedErrorException(e);
        }
    }

    @Override
    public boolean exists(String promoId) {
        try (var connection = dataSource.getConnection()) {
            try (var statement = connection.prepareStatement("SELECT EXISTS(SELECT id FROM promo WHERE id = ?)")) {
                statement.setString(1, promoId);
                try (var resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1) == 1;
                    }
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new UnexpectedErrorException(e);
        }
    }

    @Override
    public void claim(PromoClaimRequest request) {
        try {
            var claimPromo = Retry.decorateRunnable(retry, () -> runClaimPromo(request));
            timeLimiter.executeFutureSupplier(() -> CompletableFuture.runAsync(claimPromo, executor));
        } catch (Exception e) {
            throw new UnexpectedErrorException(e);
        }
    }

    @Override
    public void reset(String promoId) {
        try (var connection = dataSource.getConnection()) {
            try (var statement = connection.prepareStatement("CALL reset_promo(?)")) {
                statement.setString(1, promoId);
                statement.execute();
            }
        } catch (SQLException e) {
            throw new UnexpectedErrorException(e);
        }
    }

    @Override
    public void delete(String promoId) {
        try (var connection = dataSource.getConnection()) {
            try (var statement = connection.prepareStatement("DELETE FROM promo WHERE id = ?")) {
                statement.setString(1, promoId);
                statement.execute();
            }
        } catch (SQLException e) {
            throw new UnexpectedErrorException(e);
        }
    }

    @Override
    public void create(Promo promo) {
        try (var connection = dataSource.getConnection()) {
            try (var statement = connection.prepareStatement("INSERT INTO promo (id, allowed_claims_per_account, allowed_claims_total, claims) VALUES (?, ?, ?, ?)")) {
                statement.setString(1, promo.getId());
                statement.setLong(2, promo.getClaimsAllowedPerAccount());
                statement.setLong(3, promo.getTotalAllowedClaims());
                statement.setLong(4, promo.getTotalClaims().get());
                statement.execute();
            }
        } catch (SQLException e) {
            throw new UnexpectedErrorException(e);
        }
    }

    private void runClaimPromo(PromoClaimRequest request) {
        try (var connection = dataSource.getConnection()) {
            try (var statement = connection.prepareStatement("CALL claim_promo(?, ?)")) {
                statement.setString(1, request.getPromoId());
                statement.setString(2, request.getAccountId());
                statement.execute();
            }
        } catch (MySQLTransactionRollbackException e) {
            throw new TransactionRollbackException(e);
        } catch (SQLException e) {
            throw new UnexpectedErrorException(e);
        }
    }

    private static TimeLimiter buildTimeLimiter() {
        TimeLimiterConfig config = TimeLimiterConfig.custom()
                .cancelRunningFuture(true)
                .timeoutDuration(Duration.ofMillis(1000))
                .build();
        return TimeLimiterRegistry.of(config).timeLimiter("claim-promo-time-limiter");
    }

    private static Retry buildRetry() {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(250))
                .retryExceptions(TransactionRollbackException.class)
                .ignoreExceptions(UnexpectedErrorException.class)
                .build();
        return RetryRegistry.of(config).retry("claim-promo-retry", config);
    }

    private static PromoAvailability toPromoAvailability(PromoClaimRequest request, ResultSet resultSet) throws SQLException {
        return PromoAvailability.builder()
                .promo(toPromo(request.getPromoId(), resultSet))
                .accountId(request.getAccountId())
                .accountClaims(new AtomicLong(resultSet.getLong("account_claims")))
                .build();
    }

    private static PromoAvailability toPromoAvailability(Promo promo, String accountId) {
        return PromoAvailability.builder()
                .promo(promo)
                .accountId(accountId)
                .accountClaims(new AtomicLong())
                .build();
    }

    private static Promo toPromo(String id, ResultSet resultSet) throws SQLException {
        return Promo.builder()
                .id(id)
                .claimsAllowedPerAccount(resultSet.getLong("allowed_claims_per_account"))
                .totalAllowedClaims(resultSet.getLong("allowed_claims_total"))
                .totalClaims(new AtomicLong(resultSet.getLong("total_claims")))
                .build();
    }

}
