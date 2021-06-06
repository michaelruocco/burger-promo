package uk.co.mruoc.promo.usecase;

import lombok.Builder;
import uk.co.mruoc.promo.entity.account.Account;
import uk.co.mruoc.promo.entity.promo.Promo;
import uk.co.mruoc.promo.entity.promo.PromoAlreadyClaimedException;
import uk.co.mruoc.promo.entity.promo.PromoClaimRequest;
import uk.co.mruoc.promo.entity.promo.PromoFinishedException;
import uk.co.mruoc.promo.usecase.account.AccountService;
import uk.co.mruoc.promo.usecase.promo.PromoService;

@Builder
public class PromoClaimService {

    private final AccountService accountService;
    private final PromoService promoService;

    public void claim(PromoClaimRequest request) {
        var account = accountService.forceFind(request.getAccountId());
        validateAvailable(request, account);
        accountService.save(account.claim(request.getPromoId()));
        promoService.claim(request);
    }

    //TODO add reset method that will remove promo from all accounts

    public void validateAvailable(PromoClaimRequest request) {
        var account = accountService.forceFind(request.getAccountId());
        validateAvailable(request, account);
    }

    private void validateAvailable(PromoClaimRequest request, Account account) {
        var promo = promoService.forceFind(request.getPromoId());
        if (!isAvailableForAccount(account, promo)) {
            throw new PromoAlreadyClaimedException(request.getPromoId(), request.getAccountId());
        }
        if (promo.isFinished()) {
            throw new PromoFinishedException(promo.getId());
        }
    }

    private boolean isAvailableForAccount(Account account, Promo promo) {
        return account.getClaims(promo.getId()) < promo.getClaimsAllowedPerAccount();
    }

}
