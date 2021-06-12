package uk.co.mruoc.promo.usecase;

public class UnexpectedErrorException extends RuntimeException {

    public UnexpectedErrorException(Throwable cause) {
        super(cause);
    }

}
