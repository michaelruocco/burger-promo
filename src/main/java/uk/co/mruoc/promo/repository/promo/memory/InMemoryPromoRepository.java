package uk.co.mruoc.promo.repository.promo.memory;

import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.promo.entity.promo.Promo;
import uk.co.mruoc.promo.entity.promo.PromoNotFoundException;
import uk.co.mruoc.promo.usecase.promo.PromoRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class InMemoryPromoRepository implements PromoRepository {

    private final Map<String, Promo> promos = new ConcurrentHashMap<>();

    @Override
    public void save(Promo promo) {
        promos.put(promo.getId(), promo);
    }

    @Override
    public void claim(String promoId) {
        var promo = forceFind(promoId);
        promos.put(promo.getId(), promo.claim());
    }

    @Override
    public void reset(String promoId) {
        var promo = forceFind(promoId);
        promos.put(promo.getId(), promo.reset());
    }

    @Override
    public Optional<Promo> find(String promoId) {
        return Optional.ofNullable(promos.get(promoId));
    }

    private Promo forceFind(String promoId) {
        return find(promoId).orElseThrow(() -> new PromoNotFoundException(promoId));
    }

}
