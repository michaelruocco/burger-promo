package uk.co.mruoc.promo.entity.promo;

public class PromoFinishedException extends RuntimeException {

    public PromoFinishedException(String promoId) {
        super(String.format("promo %s has finished", promoId));
    }

}
