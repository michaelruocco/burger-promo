package uk.co.mruoc.promo.usecase;

import lombok.Builder;
import uk.co.mruoc.promo.entity.promo.Promo;
import uk.co.mruoc.promo.entity.promo.PromoClaimRequest;
import uk.co.mruoc.promo.usecase.promo.PromoService;

@Builder
public class PromoFacade {

    private final PromoService promoService;
    private final PromoClaimService claimService;

    public Promo find(String promoId) {
        return promoService.forceFind(promoId);
    }

    public Promo reset(String promoId) {
        return promoService.reset(promoId);
    }

    public void validateAvailable(PromoClaimRequest request) {
        claimService.validateAvailable(request);
    }

    public void claim(PromoClaimRequest request) {
        claimService.claim(request);
    }

}
