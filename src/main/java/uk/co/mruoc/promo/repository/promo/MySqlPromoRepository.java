package uk.co.mruoc.promo.repository.promo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.promo.entity.promo.Promo;
import uk.co.mruoc.promo.entity.promo.PromoNotFoundException;
import uk.co.mruoc.promo.usecase.promo.PromoRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class MySqlPromoRepository implements PromoRepository {

    private final PromoEntityRepository promoRepository;
    private final PromoEntityConverter converter;

    public MySqlPromoRepository(PromoEntityRepository promoRepository) {
        this(promoRepository, new PromoEntityConverter());
    }

    @Override
    public void save(Promo promo) {
        PromoEntity entity = converter.toEntity(promo);
        promoRepository.save(entity);
    }

    @Override
    public void claim(String promoId) {
        var promo = forceFind(promoId);
        save(promo.claim());
    }

    @Override
    public void reset(String promoId) {
        var promo = forceFind(promoId);
        save(promo.reset());
    }

    @Override
    public Optional<Promo> find(String promoId) {
        Optional<PromoEntity> promoEntity = promoRepository.findById(promoId);
        return promoEntity.map(converter::toPromo);
    }

    private Promo forceFind(String id) {
        return find(id).orElseThrow(() -> new PromoNotFoundException(id));
    }

}
