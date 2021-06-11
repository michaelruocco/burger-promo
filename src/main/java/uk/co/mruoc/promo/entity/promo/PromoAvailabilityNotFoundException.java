package uk.co.mruoc.promo.entity.promo;

public class PromoAvailabilityNotFoundException extends RuntimeException {

    public PromoAvailabilityNotFoundException(String promoId, String accountId) {
        super(toMessage(promoId, accountId));
    }

    private static String toMessage(String promoId, String accountId) {
        return String.format("availability of promo %s not found for account %s", promoId, accountId);
    }

}
