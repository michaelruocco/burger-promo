package uk.co.mruoc.promo.usecase.promo;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.promo.entity.promo.Promo;
import uk.co.mruoc.promo.entity.promo.PromoAlreadyExistsException;
import uk.co.mruoc.promo.entity.promo.PromoAvailability;
import uk.co.mruoc.promo.entity.promo.PromoAvailabilityNotFoundException;
import uk.co.mruoc.promo.entity.promo.PromoClaimRequest;
import uk.co.mruoc.promo.entity.promo.PromoNotFoundException;

import java.util.Optional;

@Builder
@Slf4j
public class PromoService {

    private final PromoRepository repository;

    public void claim(PromoClaimRequest request) {
        repository.claim(request);
    }

    public void create(Promo promo) {
        String promoId = promo.getId();
        if (repository.exists(promoId)) {
            throw new PromoAlreadyExistsException(promoId);
        }
        repository.create(promo);
    }

    public void reset(String promoId) {
        repository.reset(promoId);
    }

    public Optional<Promo> find(String promoId) {
        return repository.find(promoId);
    }

    public Promo forceFind(String promoId) {
        return find(promoId).orElseThrow(() -> new PromoNotFoundException(promoId) );
    }

    public PromoAvailability findAvailability(PromoClaimRequest request) {
        return repository.findAvailability(request)
                .orElseThrow(() -> new PromoAvailabilityNotFoundException(request.getPromoId(), request.getAccountId()));
    }

}
