package uk.co.mruoc.promo.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.promo.entity.Promo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class MySqlPromoRepository implements PromoRepository {

    @PersistenceContext
    private final EntityManager entityManager;
    private final PromoEntityConverter converter;

    public MySqlPromoRepository(EntityManager entityManager) {
        this(entityManager, new PromoEntityConverter());
    }

    @Override
    @Transactional
    public void save(Promo promo) {
        PromoEntity entity = converter.toEntity(promo);
        entityManager.merge(entity);
    }

    @Override
    public Optional<Promo> find(String promoId) {
        return Optional.ofNullable(entityManager.find(PromoEntity.class, promoId))
                .map(converter::toPromo);
    }

}
