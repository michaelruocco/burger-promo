package uk.co.mruoc.promo.repository;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.promo.entity.promo.PromoClaimRequest;
import uk.co.mruoc.promo.usecase.promo.PromoRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Builder
public class ClaimRunnable implements Runnable {

    private final CountDownLatch latch;

    private final PromoRepository promoRepository;

    private final Stats readStats;
    private final Stats updateStats;

    private final PromoClaimRequest request;

    @Override
    public void run() {
        try {
            readPromo();
            updatePromo();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            latch.countDown();
        }
    }

    private void readPromo() {
        var start = Instant.now();
        try {
            promoRepository.findAvailability(request);
        } finally {
            readStats.update(Duration.between(start, Instant.now()));
        }
    }

    private void updatePromo() {
        var start = Instant.now();
        try {
            promoRepository.claim(request);
        } finally {
            updateStats.update(Duration.between(start, Instant.now()));
        }
    }

}
