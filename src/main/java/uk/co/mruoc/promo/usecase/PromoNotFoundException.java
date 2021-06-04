package uk.co.mruoc.promo.usecase;

public class PromoNotFoundException extends RuntimeException {

    public PromoNotFoundException(String promoId) {
        super(promoId);
    }

}
