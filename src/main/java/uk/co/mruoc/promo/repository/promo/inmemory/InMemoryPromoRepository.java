package uk.co.mruoc.promo.repository.promo.inmemory;

import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.promo.entity.promo.Promo;
import uk.co.mruoc.promo.usecase.promo.PromoOptimisticLockException;
import uk.co.mruoc.promo.usecase.promo.PromoRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class InMemoryPromoRepository implements PromoRepository {

    private final Map<String, Promo> promos = new ConcurrentHashMap<>();

    @Override
    public void save(Promo updated) {
        Optional<Promo> existing = find(updated.getId());
        existing.ifPresent(promo -> validateUpdate(promo, updated));
        promos.put(updated.getId(), updated);
    }

    @Override
    public boolean exists(String promoId) {
        return promos.containsKey(promoId);
    }

    @Override
    public Optional<Promo> find(String promoId) {
        return Optional.ofNullable(promos.get(promoId));
    }

    private void validateUpdate(Promo existing, Promo updated) {
        String id = updated.getId();
        if (existing.getVersion() != updated.getVersion() - 1) {
            throw new PromoOptimisticLockException(id, existing.getVersion(), updated.getVersion());
        }
    }

}
