package uk.co.mruoc.promo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uk.co.mruoc.promo.entity.promo.PromoAlreadyClaimedException;
import uk.co.mruoc.promo.entity.promo.PromoFinishedException;
import uk.co.mruoc.promo.entity.promo.PromoNotFoundException;

@RequiredArgsConstructor
@ControllerAdvice
@Slf4j
public class SpringExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> unexpectedError(Throwable cause) {
        log.error(cause.getMessage(), cause);
        return new ResponseEntity<>(cause.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PromoAlreadyClaimedException.class)
    public ResponseEntity<String> alreadyClaimed(PromoAlreadyClaimedException cause) {
        return new ResponseEntity<>(cause.getMessage(), HttpStatus.GONE);
    }

    @ExceptionHandler(PromoFinishedException.class)
    public ResponseEntity<String> promoFinished(PromoFinishedException cause) {
        return new ResponseEntity<>(cause.getMessage(), HttpStatus.GONE);
    }

    @ExceptionHandler(PromoNotFoundException.class)
    public ResponseEntity<String> promoNotFound(PromoNotFoundException cause) {
        return new ResponseEntity<>(cause.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ClientAbortException.class)
    public void handleClientAbortException(ClientAbortException e) {
        log.warn(e.getMessage());
    }

}
