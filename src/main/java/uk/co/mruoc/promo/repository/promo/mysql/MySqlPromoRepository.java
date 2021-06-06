package uk.co.mruoc.promo.repository.promo.mysql;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.promo.entity.promo.Promo;
import uk.co.mruoc.promo.entity.promo.PromoNotFoundException;
import uk.co.mruoc.promo.usecase.promo.PromoRepository;

import java.time.Instant;
import java.util.Optional;

import static uk.co.mruoc.duration.calculator.DurationCalculatorUtils.millisBetweenNowAnd;

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
        var start = Instant.now();
        try {
            PromoEntity entity = converter.toEntity(promo);
            promoRepository.save(entity);
        } finally {
            log.info("save promo took {}ms to complete", millisBetweenNowAnd(start));
        }
    }

    @Override
    public void claim(String promoId) {
        var start = Instant.now();
        try {
            var promo = forceFind(promoId);
            save(promo.claim());
        } finally {
            log.info("claim promo took {}ms to complete", millisBetweenNowAnd(start));
        }
    }

    @Override
    public void reset(String promoId) {
        var start = Instant.now();
        try {
            var promo = forceFind(promoId);
            save(promo.reset());
        } finally {
            log.info("reset promo took {}ms to complete", millisBetweenNowAnd(start));
        }
    }

    @Override
    public Optional<Promo> find(String promoId) {
        var start = Instant.now();
        try {
            Optional<PromoEntity> promoEntity = promoRepository.findById(promoId);
            return promoEntity.map(converter::toPromo);
        } finally {
            log.info("find promo took {}ms to complete", millisBetweenNowAnd(start));
        }
    }

    private Promo forceFind(String id) {
        return find(id).orElseThrow(() -> new PromoNotFoundException(id));
    }

}
