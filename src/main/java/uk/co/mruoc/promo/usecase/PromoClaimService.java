package uk.co.mruoc.promo.usecase;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.promo.entity.promo.Promo;
import uk.co.mruoc.promo.entity.promo.PromoClaimRequest;
import uk.co.mruoc.promo.usecase.account.AccountService;
import uk.co.mruoc.promo.usecase.promo.PromoService;

@Builder
@Slf4j
public class PromoClaimService {

    private final PromoService promoService;
    private final AccountService accountService;

    public void claim(PromoClaimRequest request) {
        validateAvailable(request);
        promoService.claim(request);
    }

    public void validateAvailable(PromoClaimRequest request) {
        var availability = promoService.findAvailability(request);
        availability.validateAvailable();
    }

    public Promo reset(String promoId) {
        promoService.reset(promoId);
        return promoService.forceFind(promoId);
    }

}
