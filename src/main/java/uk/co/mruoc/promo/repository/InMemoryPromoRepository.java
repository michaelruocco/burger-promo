package uk.co.mruoc.promo.repository;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.promo.entity.Promotion;
import uk.co.mruoc.promo.usecase.PromoRequest;
import uk.co.mruoc.promo.usecase.PromotionNotFoundException;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class InMemoryPromoRepository implements PromoRepository {

    private final Map<String, Promotion> promotions;

    public InMemoryPromoRepository() {
        this(new ConcurrentHashMap<>());
    }

    @Override
    public void create(Promotion promotion) {
        promotions.put(promotion.getId(), promotion);
    }

    @Override
    public Optional<Promotion> find(String promoId) {
        return Optional.ofNullable(promotions.get(promoId));
    }

}
