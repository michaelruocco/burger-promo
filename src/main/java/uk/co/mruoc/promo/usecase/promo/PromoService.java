package uk.co.mruoc.promo.usecase.promo;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.promo.entity.promo.Promo;
import uk.co.mruoc.promo.entity.promo.PromoAlreadyExistsException;
import uk.co.mruoc.promo.entity.promo.PromoNotFoundException;

import java.util.Optional;

@Builder
@Slf4j
public class PromoService {

    private final PromoRepository repository;

    public void claim(Promo promo) {
        var updatedPromo = promo.claim();
        repository.save(updatedPromo);
    }

    public void create(Promo promo) {
        String promoId = promo.getId();
        if (repository.exists(promoId)) {
            throw new PromoAlreadyExistsException(promoId);
        }
        repository.save(promo);
    }

    public Promo reset(String promoId) {
        var promo = forceFind(promoId);
        var resetPromo = promo.reset();
        repository.save(resetPromo);
        return resetPromo;
    }

    public Promo forceFind(String promoId) {
        return find(promoId).orElseThrow(() -> new PromoNotFoundException(promoId));
    }

    public Optional<Promo> find(String promoId) {
        return repository.find(promoId);
    }

}
