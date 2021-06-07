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

import java.time.Instant;

import static uk.co.mruoc.duration.logger.MongoMdcDurationLoggerUtils.logDuration;

@Builder
@Slf4j
public class PromoClaimService {

    private final PromoService promoService;
    private final AccountService accountService;

    public void claim(PromoClaimRequest request) {
        var start = Instant.now();
        try {
            var promo = promoService.forceFind(request.getPromoId());
            var account = accountService.forceFind(request.getAccountId());
            validateAvailable(promo, account);
            promoService.claim(request, promo);
            accountService.claim(request);
        } finally {
            logDuration("service-claim", start);
        }
    }

    public Promo reset(String promoId) {
        var resetPromo = promoService.reset(promoId);
        accountService.resetAccountClaims(promoId);
        return resetPromo;
    }

    public void validateAvailable(PromoClaimRequest request) {
        var start = Instant.now();
        try {
            var promo = promoService.forceFind(request.getPromoId());
            var account = accountService.forceFind(request.getAccountId());
            validateAvailable(promo, account);
        } finally {
            logDuration("check-available", start);
        }
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
        return account.getClaimsCount(promo.getId()) < promo.getClaimsAllowedPerAccount();
    }

}
