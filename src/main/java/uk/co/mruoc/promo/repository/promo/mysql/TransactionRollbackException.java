package uk.co.mruoc.promo.repository.promo.mysql;

import com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException;

public class TransactionRollbackException extends RuntimeException {

    public TransactionRollbackException(MySQLTransactionRollbackException cause) {
        super(cause);
    }

}
