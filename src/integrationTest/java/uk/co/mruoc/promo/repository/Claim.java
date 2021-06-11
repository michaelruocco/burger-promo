package uk.co.mruoc.promo.repository;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.promo.entity.promo.PromoClaimRequest;
import uk.co.mruoc.promo.usecase.account.AccountRepository;
import uk.co.mruoc.promo.usecase.promo.PromoRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Builder
public class Claim implements Runnable {

    private final CountDownLatch latch;

    private final AccountRepository accountRepository;
    private final PromoRepository promoRepository;

    private final Stats readAccountStats;

    private final Stats readPromoStats;
    private final Stats updatePromoStats;

    private final PromoClaimRequest request;

    @Override
    public void run() {
        try {
            readAccount();
            readPromo();

            updatePromo();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            latch.countDown();
        }
    }

    private void readAccount() {
        var start = Instant.now();
        try {
            accountRepository.find(request.getAccountId());
        } finally {
            readAccountStats.update(Duration.between(start, Instant.now()));
        }
    }

    private void readPromo() {
        var start = Instant.now();
        try {
            promoRepository.find(request.getPromoId());
        } finally {
            readPromoStats.update(Duration.between(start, Instant.now()));
        }
    }

    private void updatePromo() {
        var start = Instant.now();
        try {
            promoRepository.claim(request);
        } finally {
            updatePromoStats.update(Duration.between(start, Instant.now()));
        }
    }

}
