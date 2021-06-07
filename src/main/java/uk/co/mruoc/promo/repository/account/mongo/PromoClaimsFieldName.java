package uk.co.mruoc.promo.repository.account.mongo;

public class PromoClaimsFieldName {

    private PromoClaimsFieldName() {
        // utility class
    }

    public static String build(String promoId) {
        return String.format("promoClaims.%s", promoId);
    }

}
