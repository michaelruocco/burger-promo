package uk.co.mruoc.promo.entity.promo;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Builder
@Data
public class PromoAvailability {

    private final String accountId;
    private final AtomicLong accountClaims;
    private final Promo promo;

    public void validateAvailable() {
        validateAccountHasClaimsRemaining();
        promo.validateFinished();
    }

    private void validateAccountHasClaimsRemaining() {
        if (getAccountClaims() >= promo.getClaimsAllowedPerAccount()) {
            throw new PromoAlreadyClaimedException(promo.getId(), accountId);
        }
    }

    private long getAccountClaims() {
        return Optional.ofNullable(accountClaims)
                .map(AtomicLong::get)
                .orElse(0L);
    }

}
