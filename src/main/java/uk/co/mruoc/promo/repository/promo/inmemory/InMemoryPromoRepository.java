package uk.co.mruoc.promo.repository.promo.inmemory;

import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.promo.entity.promo.Promo;
import uk.co.mruoc.promo.entity.promo.PromoClaimRequest;
import uk.co.mruoc.promo.usecase.promo.PromoRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class InMemoryPromoRepository implements PromoRepository {

    private final Map<String, Promo> promos = new HashMap<>();
    private final Map<String, AtomicLong> promoClaimCounts = new ConcurrentHashMap<>();

    @Override
    public void claim(PromoClaimRequest request) {
        String promoId = request.getPromoId();
        findClaimCount(promoId).incrementAndGet();
    }

    @Override
    public void reset(String promoId) {
        Optional.ofNullable(promoClaimCounts.get(promoId)).ifPresent(count -> count.set(0));
    }

    @Override
    public void create(Promo updated) {
        promos.put(updated.getId(), updated);
    }

    @Override
    public boolean exists(String promoId) {
        return promos.containsKey(promoId);
    }

    @Override
    public Optional<Promo> find(String promoId) {
        return Optional.ofNullable(promos.get(promoId))
                .map(promo -> promo.withTotalClaims(findClaimCount(promoId)));
    }

    private AtomicLong findClaimCount(String promoId) {
        Optional<AtomicLong> existingCount = Optional.ofNullable(promoClaimCounts.get(promoId));
        if (existingCount.isEmpty()) {
            var newCount = new AtomicLong();
            promoClaimCounts.put(promoId, newCount);
            return newCount;
        }
        return existingCount.get();
    }

}
