package uk.co.mruoc.promo.usecase.promo;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.promo.entity.promo.Promo;
import uk.co.mruoc.promo.entity.promo.PromoAlreadyExistsException;
import uk.co.mruoc.promo.entity.promo.PromoClaimRequest;
import uk.co.mruoc.promo.entity.promo.PromoNotFoundException;

import java.util.Optional;

@Builder
@Slf4j
public class PromoService {

    private final PromoRepository repository;

    public void claim(PromoClaimRequest request, Promo promo) {
        promo.validateFinished();
        repository.claim(request);
    }

    public void create(Promo promo) {
        String promoId = promo.getId();
        if (repository.exists(promoId)) {
            throw new PromoAlreadyExistsException(promoId);
        }
        repository.create(promo);
    }

    public Promo reset(String promoId) {
        repository.reset(promoId);
        return forceFind(promoId);
    }

    public Promo forceFind(String promoId) {
        return find(promoId).orElseThrow(() -> new PromoNotFoundException(promoId));
    }

    public Optional<Promo> find(String promoId) {
        return repository.find(promoId);
    }

}
