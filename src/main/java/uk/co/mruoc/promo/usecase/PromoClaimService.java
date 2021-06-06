package uk.co.mruoc.promo.usecase;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.promo.entity.account.Account;
import uk.co.mruoc.promo.entity.promo.Promo;
import uk.co.mruoc.promo.entity.promo.PromoAlreadyClaimedException;
import uk.co.mruoc.promo.entity.promo.PromoClaimRequest;
import uk.co.mruoc.promo.entity.promo.PromoFinishedException;
import uk.co.mruoc.promo.usecase.account.AccountService;
import uk.co.mruoc.promo.usecase.promo.PromoService;

@Builder
@Slf4j
public class PromoClaimService {

    private final PromoService promoService;
    private final AccountService accountService;

    public synchronized void claim(PromoClaimRequest request) {
        var promo = promoService.forceFind(request.getPromoId());
        var account = accountService.forceFind(request.getAccountId());
        validateAvailable(promo, account);
        var updatedAccount = account.claim(request.getPromoId());
        persistClaim(promo, updatedAccount);
    }

    public synchronized Promo reset(String promoId) {
        var resetPromo = promoService.reset(promoId);
        accountService.resetPromo(promoId);
        return resetPromo;
    }

    public void validateAvailable(PromoClaimRequest request) {
        var promo = promoService.forceFind(request.getPromoId());
        var account = accountService.forceFind(request.getAccountId());
        validateAvailable(promo, account);
    }

    private void validateAvailable(Promo promo, Account account) {
        if (!isPromoAvailableForAccount(promo, account)) {
            throw new PromoAlreadyClaimedException(promo.getId(), account.getId());
        }
        if (promo.isFinished()) {
            throw new PromoFinishedException(promo.getId());
        }
    }

    private boolean isPromoAvailableForAccount(Promo promo, Account account) {
        return account.getClaims(promo.getId()) < promo.getClaimsAllowedPerAccount();
    }

    private synchronized void persistClaim(Promo promo, Account updatedAccount) {
        promoService.claim(promo);
        accountService.save(updatedAccount);
    }

}
