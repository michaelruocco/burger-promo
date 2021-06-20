package uk.co.mruoc.promo.repository.account.mysql;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.promo.entity.account.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class AccountUpdater {

    private final BatchBuilder batchBuilder;

    public AccountUpdater() {
        this(new BatchBuilder());
    }

    public void update(Connection connection, Collection<Account> accounts) throws SQLException {
        try (var statement = connection.prepareStatement("INSERT INTO account (id) VALUES (?)")) {
            List<List<Account>> batches = batchBuilder.toBatches(accounts);
            update(statement, batches);
        }
    }

    private void update(PreparedStatement statement, List<List<Account>> batches) throws SQLException {
        for (var batch : batches) {
            for (var account : batch) {
                statement.setString(1, account.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

}
