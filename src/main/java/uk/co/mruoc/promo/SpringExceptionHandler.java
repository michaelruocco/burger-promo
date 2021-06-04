package uk.co.mruoc.promo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uk.co.mruoc.promo.entity.AlreadyClaimedException;
import uk.co.mruoc.promo.entity.PromoFinishedException;
import uk.co.mruoc.promo.usecase.PromoNotFoundException;

@RequiredArgsConstructor
@ControllerAdvice
@Slf4j
public class SpringExceptionHandler {

    @ExceptionHandler(AlreadyClaimedException.class)
    public ResponseEntity<String> alreadyClaimed(AlreadyClaimedException cause) {
        return new ResponseEntity<>("already claimed", HttpStatus.GONE);
    }

    @ExceptionHandler(PromoFinishedException.class)
    public ResponseEntity<String> promoFinished(PromoFinishedException cause) {
        return new ResponseEntity<>("none remaining", HttpStatus.GONE);
    }

    @ExceptionHandler(PromoNotFoundException.class)
    public ResponseEntity<String> notFound(PromoNotFoundException cause) {
        var message = String.format("promo %s not found", cause.getMessage());
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

}
