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
import java.util.Optional;

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
                    return toAccount(resultSet);
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
                        log.info("executing batch {}", batchCount);
                        statement.executeBatch();
                        batchCount = 0;
                    }
                }
                if (batchCount > 0) {
                    log.info("executing batch {}", batchCount);
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

    private static Optional<Account> toAccount(ResultSet resultSet) throws SQLException {
        if (!resultSet.isBeforeFirst()) {
            return Optional.empty();
        }
        return Optional.of(Account.builder()
                .id(resultSet.getString("id"))
                .build());
    }

}
