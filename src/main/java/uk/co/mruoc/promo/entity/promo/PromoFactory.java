package uk.co.mruoc.promo.entity.promo;

public class PromoFactory {

    public Promo buildFreeBurgers() {
        return Promo.builder()
                .id("free-burgers")
                .totalAllowedClaims(6000000)
                .claimsAllowedPerAccount(1)
                .build();
    }

}
