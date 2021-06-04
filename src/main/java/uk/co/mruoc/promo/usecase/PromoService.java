package uk.co.mruoc.promo.usecase;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.promo.entity.Promo;
import uk.co.mruoc.promo.repository.PromoRepository;

@RequiredArgsConstructor
public class PromoService {

    private final PromoRepository repository;

    public void save(Promo promo) {
        repository.save(promo);
    }

    public boolean anyRemaining(PromoRequest request) {
        return get(request.getPromoId()).anyRemaining(request.getAccountId());
    }

    public void claim(PromoRequest request) {
        var promo = get(request.getPromoId());
        repository.save(promo.claim(request.getAccountId()));
    }

    public Promo get(String promoId) {
        return repository.find(promoId).orElseThrow(() -> new PromoNotFoundException(promoId));
    }

}
