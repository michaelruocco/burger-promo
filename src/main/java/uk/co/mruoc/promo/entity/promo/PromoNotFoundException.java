package uk.co.mruoc.promo.entity.promo;

public class PromoNotFoundException extends RuntimeException {

    public PromoNotFoundException(String id) {
        super(String.format("promo %s not found", id));
    }

}
