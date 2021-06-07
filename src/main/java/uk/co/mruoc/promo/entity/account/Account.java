package uk.co.mruoc.promo.entity.account;

import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Builder(toBuilder = true)
@Data
public class Account {

    private final String id;

    @Builder.Default
    private final Map<String, AtomicLong> promoClaims = new ConcurrentHashMap<>();

    public void claim(String promoId) {
        getAtomicClaimsCount(promoId).incrementAndGet();
    }

    public void resetClaims(String promoId) {
        getAtomicClaimsCount(promoId).set(0);
    }

    public boolean hasClaims(String promoId) {
        return getClaimsCount(promoId) > 0;
    }

    public long getClaimsCount(String promoId) {
        return getAtomicClaimsCount(promoId).get();
    }

    private AtomicLong getAtomicClaimsCount(String promoId) {
        Optional<AtomicLong> existingCount = Optional.ofNullable(promoClaims.get(promoId));
        if (existingCount.isEmpty()) {
            var newCount = new AtomicLong();
            promoClaims.put(promoId, newCount);
            return newCount;
        }
        return existingCount.get();
    }

}
