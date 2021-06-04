package uk.co.mruoc.promo.repository;

import uk.co.mruoc.promo.entity.Promotion;

import java.util.Optional;

public interface PromoRepository {

    void create(Promotion promotion);

    Optional<Promotion> find(String promoId);

}
