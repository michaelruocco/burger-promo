package uk.co.mruoc.promo.entity.promo;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.With;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
@Data
@Builder
@Slf4j
public class Promo {

    private final String id;
    private final long totalAllowedClaims;
    private final long claimsAllowedPerAccount;

    @With
    @Builder.Default
    private final AtomicLong totalClaims = new AtomicLong();

    public boolean isFinished() {
        return totalClaims.get() >= totalAllowedClaims;
    }

    public long getRemaining() {
        long remaining = totalAllowedClaims - totalClaims.get();
        return Math.max(remaining, 0);
    }

    public void validateFinished() {
        if (isFinished()) {
            throw new PromoFinishedException(id);
        }
    }

}
