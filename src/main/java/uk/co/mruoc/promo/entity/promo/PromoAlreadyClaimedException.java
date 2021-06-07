package uk.co.mruoc.promo.entity.promo;

public class PromoAlreadyClaimedException extends RuntimeException {

    public PromoAlreadyClaimedException(String promoId, String accountId) {
        super(toMessage(promoId, accountId));
    }

    private static String toMessage(String promoId, String accountId) {
        return String.format("account %s has already claimed promo %s", accountId, promoId);
    }

}
