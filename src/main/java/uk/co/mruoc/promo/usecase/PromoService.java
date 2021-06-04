package uk.co.mruoc.promo.usecase;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.promo.entity.Promotion;
import uk.co.mruoc.promo.repository.PromoRepository;

@RequiredArgsConstructor
public class PromoService {

    private final PromoRepository repository;

    public void create(Promotion promotion) {
        repository.create(promotion);
    }

    public boolean anyRemaining(PromoRequest request) {
        return get(request.getPromoId()).anyRemaining(request.getAccountId());
    }

    public void claim(PromoRequest request) {
        get(request.getPromoId()).claim(request.getAccountId());
    }

    public Promotion get(String promoId) {
        return repository.find(promoId).orElseThrow(() -> new PromotionNotFoundException(promoId));
    }

}
