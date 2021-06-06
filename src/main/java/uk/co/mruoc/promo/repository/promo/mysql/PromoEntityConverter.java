package uk.co.mruoc.promo.repository.promo.mysql;

import uk.co.mruoc.promo.entity.promo.Promo;

public class PromoEntityConverter {

    public PromoEntity toEntity(Promo promo) {
        return new PromoEntity(
                promo.getId(),
                promo.getTotalAllowedClaims(),
                promo.getClaimsAllowedPerAccount(),
                promo.getTotalClaims(),
                promo.getVersion()
        );
    }

    public Promo toPromo(PromoEntity entity) {
        return Promo.builder()
                .id(entity.getId())
                .claimsAllowedPerAccount(entity.getClaimsAllowedPerAccount())
                .totalAllowedClaims(entity.getTotalAllowedClaims())
                .totalClaims(entity.getTotalClaims())
                .version(entity.getVersion())
                .build();
    }

}
