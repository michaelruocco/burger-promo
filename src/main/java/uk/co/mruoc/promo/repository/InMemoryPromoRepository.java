package uk.co.mruoc.promo.repository;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.promo.entity.Promo;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class InMemoryPromoRepository implements PromoRepository {

    private final Map<String, Promo> promotions;

    public InMemoryPromoRepository() {
        this(new ConcurrentHashMap<>());
    }

    @Override
    public void save(Promo promo) {
        promotions.put(promo.getId(), promo);
    }

    @Override
    public Optional<Promo> find(String promoId) {
        return Optional.ofNullable(promotions.get(promoId));
    }

}
