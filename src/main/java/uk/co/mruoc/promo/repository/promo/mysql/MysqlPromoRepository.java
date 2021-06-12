package uk.co.mruoc.promo.repository.promo.mysql;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.promo.entity.promo.Promo;
import uk.co.mruoc.promo.entity.promo.PromoAvailability;
import uk.co.mruoc.promo.entity.promo.PromoClaimRequest;
import uk.co.mruoc.promo.usecase.UnexpectedErrorException;
import uk.co.mruoc.promo.usecase.promo.PromoRepository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
public class MysqlPromoRepository implements PromoRepository {

    private final DataSource dataSource;

    @Override
    public Optional<PromoAvailability> findAvailability(PromoClaimRequest request) {
        try (var connection = dataSource.getConnection()) {
            try (var statement = connection.prepareStatement("CALL get_promo_availability(?, ?)")) {
                statement.setString(1, request.getPromoId());
                statement.setString(2, request.getAccountId());
                try (var resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return toPromoAvailability(request, resultSet);
                    }
                    return Optional.empty();
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
        try (var connection = dataSource.getConnection()) {
            try (var statement = connection.prepareStatement("CALL claim_promo(?, ?)")) {
                statement.setString(1, request.getPromoId());
                statement.setString(2, request.getAccountId());
                statement.execute();
            }
        } catch (SQLException e) {
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
            try (var statement = connection.prepareStatement("INSERT INTO promo (id, claims_allowed_per_account, total_allowed_claims) VALUES (?, ?, ?)")) {
                statement.setString(1, promo.getId());
                statement.setLong(2, promo.getClaimsAllowedPerAccount());
                statement.setLong(3, promo.getTotalAllowedClaims());
                statement.execute();
            }
        } catch (SQLException e) {
            throw new UnexpectedErrorException(e);
        }
    }

    private static Optional<PromoAvailability> toPromoAvailability(PromoClaimRequest request, ResultSet resultSet) throws SQLException {
        return Optional.of(PromoAvailability.builder()
                .accountId(request.getAccountId())
                .accountClaims(new AtomicLong(resultSet.getLong("account_claims")))
                .build());
    }

    private static Promo toPromo(String id, ResultSet resultSet) throws SQLException {
        return Promo.builder()
                .id(id)
                .claimsAllowedPerAccount(resultSet.getLong("claims_allowed_per_account"))
                .totalAllowedClaims(resultSet.getLong("total_allowed_claims"))
                .totalClaims(new AtomicLong(resultSet.getLong("total_claims")))
                .build();
    }

}
