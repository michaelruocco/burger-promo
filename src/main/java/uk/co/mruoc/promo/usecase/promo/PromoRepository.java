package uk.co.mruoc.promo.usecase.promo;

import uk.co.mruoc.promo.entity.promo.Promo;
import uk.co.mruoc.promo.entity.promo.PromoAvailability;
import uk.co.mruoc.promo.entity.promo.PromoClaimRequest;

import java.util.Optional;

public interface PromoRepository {

    void claim(PromoClaimRequest request);

    void reset(String promoId);

    void create(Promo promo);

    void delete(String promoId);

    Optional<PromoAvailability> findAvailability(PromoClaimRequest request);

    Optional<Promo> find(String promoId);

    boolean exists(String promoId);

}
