package uk.co.mruoc.promo.repository;

import uk.co.mruoc.promo.entity.Promo;

import java.util.Optional;

public interface PromoRepository {

    void save(Promo promo);

    Optional<Promo> find(String promoId);

}
