package uk.co.mruoc.promo.entity.promo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PromoClaimRequest {

    private final String promoId;
    private final String accountId;

}
