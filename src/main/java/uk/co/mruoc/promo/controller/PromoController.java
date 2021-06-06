package uk.co.mruoc.promo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.mruoc.promo.entity.promo.Promo;
import uk.co.mruoc.promo.entity.promo.PromoClaimRequest;
import uk.co.mruoc.promo.usecase.PromoFacade;

import java.time.Instant;

import static uk.co.mruoc.duration.calculator.DurationCalculatorUtils.millisBetweenNowAnd;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/promos")
@Slf4j
public class PromoController {

    private final PromoFacade promoFacade;

    @GetMapping("/{promoId}")
    public Promo getPromo(@PathVariable("promoId") String promoId) {
        return promoFacade.find(promoId);
    }

    @PutMapping("/{promoId}")
    public Promo resetPromo(@PathVariable("promoId") String promoId) {
        return promoFacade.reset(promoId);
    }

    @GetMapping("/{promoId}/accounts/{accountId}")
    public ResponseEntity<Void> isAvailable(@PathVariable("promoId") String promoId, @PathVariable("accountId") String accountId) {
        var start = Instant.now();
        try {
            PromoClaimRequest request = toRequest(promoId, accountId);
            promoFacade.validateAvailable(request);
            return ResponseEntity.ok().build();
        } finally {
            log.info("is available took {}ms", millisBetweenNowAnd(start));
        }
    }

    @PostMapping("/{promoId}/accounts/{accountId}")
    public ResponseEntity<Void> claim(@PathVariable("promoId") String promoId, @PathVariable("accountId") String accountId) {
        var start = Instant.now();
        try {
            PromoClaimRequest request = toRequest(promoId, accountId);
            promoFacade.claim(request);
            return ResponseEntity.accepted().build();
        } finally {
            log.info("claim took {}ms", millisBetweenNowAnd(start));
        }
    }

    private static PromoClaimRequest toRequest(String promoId, String accountId) {
        return PromoClaimRequest.builder()
                .promoId(promoId)
                .accountId(accountId)
                .build();
    }

}
