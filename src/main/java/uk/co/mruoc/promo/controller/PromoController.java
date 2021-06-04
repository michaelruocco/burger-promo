package uk.co.mruoc.promo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.mruoc.promo.entity.Promotion;
import uk.co.mruoc.promo.usecase.PromoRequest;
import uk.co.mruoc.promo.usecase.PromoService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/promos")
public class PromoController {

    private final PromoService service;

    @GetMapping("/{promoId}")
    public Promotion getPromo(@PathVariable("promoId") String promoId) {
        return service.get(promoId);
    }

    @GetMapping("/{promoId}/accounts/{accountId}")
    public boolean anyRemaining(@PathVariable("promoId") String promoId, @PathVariable("accountId") String accountId) {
        PromoRequest request = toRequest(promoId, accountId);
        return service.anyRemaining(request);
    }

    @PostMapping("/{promoId}/accounts/{accountId}")
    public ResponseEntity<Void> claim(@PathVariable("promoId") String promoId, @PathVariable("accountId") String accountId) {
        PromoRequest request = toRequest(promoId, accountId);
        service.claim(request);
        return ResponseEntity.accepted().build();
    }

    private static PromoRequest toRequest(String promoId, String accountId) {
        return PromoRequest.builder()
                .promoId(promoId)
                .accountId(accountId)
                .build();
    }

}
