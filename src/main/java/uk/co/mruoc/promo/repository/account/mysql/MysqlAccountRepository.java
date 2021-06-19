package uk.co.mruoc.promo.repository.account.mysql;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.promo.entity.account.Account;
import uk.co.mruoc.promo.entity.account.AccountNotFoundException;
import uk.co.mruoc.promo.usecase.account.AccountRepository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RequiredArgsConstructor
public class MysqlAccountRepository implements AccountRepository  {

    private final DataSource dataSource;

    @Override
    public Optional<Account> find(String id) {
        try(var connection = dataSource.getConnection()) {
            try (var statement = connection.prepareStatement("SELECT id FROM account WHERE id = ?")) {
                statement.setString(1, id);
                try (var resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(loadAccountClaims(resultSet));
                    }
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new AccountNotFoundException(e);
        }
    }

    @Override
    public void saveAll(Collection<Account> accounts) {
        try(var connection = dataSource.getConnection()) {
            try (var statement = connection.prepareStatement("INSERT INTO account (id) VALUES (?)")) {
                //TODO clean up batches
                var batchCount = 0;
                for (var account : accounts) {
                    statement.setString(1, account.getId());
                    statement.addBatch();
                    batchCount++;
                    if (batchCount % 1000 == 0) {
                        statement.executeBatch();
                        batchCount = 0;
                    }
                }
                if (batchCount > 0) {
                    statement.executeBatch();
                }
            }
        } catch (SQLException e) {
            throw new AccountNotFoundException(e);
        }
    }

    @Override
    public void deleteAll() {
        try(var connection = dataSource.getConnection()) {
            try (var statement = connection.prepareStatement("DELETE FROM account")) {
                statement.execute();
            }
        } catch (SQLException e) {
            throw new AccountNotFoundException(e);
        }
    }

    private Map<String, AtomicLong> loadAccountPromoClaims(String accountId) {
        try(var connection = dataSource.getConnection()) {
            try (var statement = connection.prepareStatement("SELECT promo_id, claims FROM account_claim WHERE account_id = ?")) {
                statement.setString(1, accountId);
                try (var resultSet = statement.executeQuery()) {
                    Map<String, AtomicLong> accountClaims = new ConcurrentHashMap<>();
                    while (resultSet.next()) {
                        accountClaims.put(resultSet.getString("promo_id"), new AtomicLong(resultSet.getLong("claims")));
                    }
                    return accountClaims;
                }
            }
        } catch (SQLException e) {
            throw new AccountNotFoundException(e);
        }
    }

    private Account loadAccountClaims(ResultSet resultSet) throws SQLException {
        var accountId = resultSet.getString("id");
        return Account.builder()
                .id(accountId)
                .promoClaims(loadAccountPromoClaims(accountId))
                .build();
    }

}
