package uk.co.mruoc.promo.entity.promo;

public class PromoAlreadyClaimedException extends RuntimeException {

    public PromoAlreadyClaimedException(String promoId, String accountId) {
        super(String.format("account %s has already claimed promo %s", accountId, promoId));
    }

}
