package uk.co.mruoc.promo.usecase.promo;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.promo.entity.promo.Promo;
import uk.co.mruoc.promo.entity.promo.PromoClaimRequest;
import uk.co.mruoc.promo.entity.promo.PromoNotFoundException;

import java.util.Optional;
import java.util.concurrent.ExecutorService;

@Builder
@Slf4j
public class PromoService {

    private final PromoRepository repository;
    private final ExecutorService executorService;

    public void claim(PromoClaimRequest request) {
        executorService.submit(() -> {
                    var promo = forceFind(request.getPromoId());
                    repository.save(promo.claim());
                }
        );
    }

    public void create(Promo promo) {
        //TODO error if already exists
        repository.save(promo);
    }

    public Promo reset(String promoId) {
        var promo = forceFind(promoId);
        repository.save(promo.reset());
        return forceFind(promoId);
    }

    public Promo forceFind(String promoId) {
        return find(promoId).orElseThrow(() -> new PromoNotFoundException(promoId));
    }

    public Optional<Promo> find(String promoId) {
        return repository.find(promoId);
    }

}
