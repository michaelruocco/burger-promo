package uk.co.mruoc.promo.repository;

import uk.co.mruoc.promo.entity.Promo;

public class PromoEntityConverter {

    public PromoEntity toEntity(Promo promo) {
        return new PromoEntity(
                promo.getId(),
                promo.getTotalAllowed(),
                promo.getClaimedAccountIds(),
                promo.getVersion()
        );
    }

    public Promo toPromo(PromoEntity entity) {
        return Promo.builder()
                .id(entity.getId())
                .totalAllowed(entity.getTotalAllowed())
                .claimedAccountIds(entity.getClaimedAccountIds())
                .version(entity.getVersion())
                .build();
    }

}
