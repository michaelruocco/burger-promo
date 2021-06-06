package uk.co.mruoc.promo.entity.promo;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Data
@Builder(toBuilder = true)
@Slf4j
public class Promo {

    private final String id;
    private final long totalAllowedClaims;
    private final long claimsAllowedPerAccount;
    private final long totalClaims;
    private final long version;

    public boolean isFinished() {
        return totalClaims >= totalAllowedClaims;
    }

    public Promo reset() {
        return toBuilder().totalClaims(0).build();
    }

    public long getRemaining() {
        return totalAllowedClaims - totalClaims;
    }

    public Promo claim() {
        if (isFinished()) {
            throw new PromoFinishedException(id);
        }
        return toBuilder().totalClaims(totalClaims + 1).build();
    }

}
