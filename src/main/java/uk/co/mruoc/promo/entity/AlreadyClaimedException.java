package uk.co.mruoc.promo.entity;

public class AlreadyClaimedException extends RuntimeException {

    public AlreadyClaimedException(String promoId) {
        super(promoId);
    }

}
