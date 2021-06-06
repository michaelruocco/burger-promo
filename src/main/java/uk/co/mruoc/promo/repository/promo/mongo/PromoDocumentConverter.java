package uk.co.mruoc.promo.repository.promo.mongo;

import uk.co.mruoc.promo.entity.promo.Promo;

public class PromoDocumentConverter {

    public Promo toPromo(PromoDocument document) {
        return Promo.builder()
                .id(document.getId())
                .totalAllowedClaims(document.getTotalAllowedClaims())
                .claimsAllowedPerAccount(document.getClaimsAllowedPerAccount())
                .totalClaims(document.getTotalClaims())
                .version(document.getVersion())
                .build();
    }

    public PromoDocument toDocument(Promo promo) {
        return PromoDocument.builder()
                .id(promo.getId())
                .totalAllowedClaims(promo.getTotalAllowedClaims())
                .claimsAllowedPerAccount(promo.getClaimsAllowedPerAccount())
                .totalClaims(promo.getTotalClaims())
                .version(promo.getVersion())
                .build();
    }

}
