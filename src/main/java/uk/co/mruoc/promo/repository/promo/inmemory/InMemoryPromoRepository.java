package uk.co.mruoc.promo.repository.promo.inmemory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.promo.entity.promo.Promo;
import uk.co.mruoc.promo.entity.promo.PromoAvailability;
import uk.co.mruoc.promo.entity.promo.PromoClaimRequest;
import uk.co.mruoc.promo.entity.promo.PromoNotFoundException;
import uk.co.mruoc.promo.repository.account.inmemory.InMemoryAccountRepository;
import uk.co.mruoc.promo.usecase.promo.PromoRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class InMemoryPromoRepository implements PromoRepository {

    private final InMemoryAccountRepository accountRepository;

    private final Map<String, Promo> promos = new HashMap<>();

    @Override
    public void claim(PromoClaimRequest request) {
        var promo = forceFind(request.getPromoId());
        promo.claim();
        accountRepository.claim(request);
    }

    @Override
    public void reset(String promoId) {
        var promo = forceFind(promoId);
        promo.reset();
        accountRepository.resetPromo(promoId);
    }

    @Override
    public void create(Promo updated) {
        promos.put(updated.getId(), updated);
    }

    @Override
    public void delete(String promoId) {
        promos.remove(promoId);
    }

    @Override
    public Optional<PromoAvailability> findAvailability(PromoClaimRequest request) {
        var promoId = request.getPromoId();
        return Optional.ofNullable(promos.get(promoId))
                .map(promo -> PromoAvailability.builder()
                        .promo(promo)
                        .accountId(request.getPromoId())
                        .accountClaims(accountRepository.findClaimsCount(request))
                        .build());
    }

    @Override
    public Optional<Promo> find(String promoId) {
        return Optional.ofNullable(promos.get(promoId));
    }

    @Override
    public boolean exists(String promoId) {
        return promos.containsKey(promoId);
    }

    private Promo forceFind(String promoId) {
        return find(promoId).orElseThrow(() -> new PromoNotFoundException(promoId));
    }

}
