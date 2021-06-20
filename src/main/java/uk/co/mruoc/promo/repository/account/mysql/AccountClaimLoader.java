package uk.co.mruoc.promo.repository.account.mysql;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.promo.entity.account.AccountNotFoundException;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
public class AccountClaimLoader {

    private static final String SQL = "SELECT promo_id, claims FROM account_claim WHERE account_id = ?";

    private final DataSource dataSource;

    public Map<String, AtomicLong> loadAccountPromoClaims(String accountId) {
        try (var connection = dataSource.getConnection()) {
            try (var statement = connection.prepareStatement(SQL)) {
                return toClaims(accountId, statement);
            }
        } catch (SQLException e) {
            throw new AccountNotFoundException(e);
        }
    }

    private static Map<String, AtomicLong> toClaims(String accountId, PreparedStatement statement) throws SQLException {
        statement.setString(1, accountId);
        try (var resultSet = statement.executeQuery()) {
            return toClaims(resultSet);
        }
    }

    private static Map<String, AtomicLong> toClaims(ResultSet rs) throws SQLException {
        Map<String, AtomicLong> claims = new ConcurrentHashMap<>();
        while (rs.next()) {
            claims.put(rs.getString("promo_id"), new AtomicLong(rs.getLong("claims")));
        }
        return claims;
    }

}
