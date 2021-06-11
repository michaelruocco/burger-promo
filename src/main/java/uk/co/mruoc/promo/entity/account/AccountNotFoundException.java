package uk.co.mruoc.promo.entity.account;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String id) {
        super(String.format("account %s not found", id));
    }

    public AccountNotFoundException(Throwable cause) {
        super(cause);
    }

}
