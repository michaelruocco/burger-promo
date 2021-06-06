package uk.co.mruoc.promo.entity.promo;

public class PromoAlreadyExistsException extends RuntimeException {

    public PromoAlreadyExistsException(String promoId) {
        super(String.format("promo %s already exists", promoId));
    }

}
