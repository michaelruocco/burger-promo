package uk.co.mruoc.promo.usecase;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PromoRequest {

    private final String promoId;
    private final String accountId;

}
