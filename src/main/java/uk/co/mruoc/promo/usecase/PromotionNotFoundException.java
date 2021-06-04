package uk.co.mruoc.promo.usecase;

public class PromotionNotFoundException extends RuntimeException {

    public PromotionNotFoundException(String promoId) {
        super(promoId);
    }

}
