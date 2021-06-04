package uk.co.mruoc.promo.entity;

public class PromoFinishedException extends RuntimeException {

    public PromoFinishedException(String promoId) {
        super(promoId);
    }

}
