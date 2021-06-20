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
public class MysqlAccountRepository implements AccountRepository {

    private final DataSource dataSource;
    private final AccountClaimLoader accountClaimLoader;
    private final AccountUpdater accountUpdater;

    public MysqlAccountRepository(DataSource dataSource) {
        this(dataSource, new AccountClaimLoader(dataSource), new AccountUpdater());
    }

    @Override
    public Optional<Account> find(String id) {
        try (var connection = dataSource.getConnection()) {
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
        try (var connection = dataSource.getConnection()) {
            accountUpdater.update(connection, accounts);
        } catch (SQLException e) {
            throw new AccountNotFoundException(e);
        }
    }

    @Override
    public void deleteAll() {
        try (var connection = dataSource.getConnection()) {
            try (var statement = connection.prepareStatement("DELETE FROM account")) {
                statement.execute();
            }
        } catch (SQLException e) {
            throw new AccountNotFoundException(e);
        }
    }

    private Account loadAccountClaims(ResultSet resultSet) throws SQLException {
        var accountId = resultSet.getString("id");
        return Account.builder()
                .id(accountId)
                .promoClaims(accountClaimLoader.loadAccountPromoClaims(accountId))
                .build();
    }

}
