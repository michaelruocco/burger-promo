package uk.co.mruoc.promo.usecase.promo;

import uk.co.mruoc.promo.entity.promo.Promo;

import java.util.Optional;

public interface PromoRepository {

    void save(Promo promo);

    boolean exists(String promoId);

    Optional<Promo> find(String promoId);

}
