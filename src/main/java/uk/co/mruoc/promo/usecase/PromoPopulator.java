package uk.co.mruoc.promo.usecase;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import uk.co.mruoc.promo.entity.PromoFactory;


@Builder
@Slf4j
public class PromoPopulator implements ApplicationListener<ContextRefreshedEvent> {

    private final PromoFactory factory;
    private final PromoService service;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        var promo = factory.buildFreeBurgers();
        log.info("saving initial promo {}", promo);
        service.save(promo);
    }
}
