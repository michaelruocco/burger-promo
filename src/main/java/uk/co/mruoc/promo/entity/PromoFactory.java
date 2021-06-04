package uk.co.mruoc.promo.entity;

public class PromoFactory {

    public Promo buildFreeBurgers() {
        return Promo.builder()
                .id("free-burgers")
                .totalAllowed(6000000)
                .build();
    }

}
