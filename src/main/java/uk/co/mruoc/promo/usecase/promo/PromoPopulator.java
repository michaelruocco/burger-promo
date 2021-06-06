package uk.co.mruoc.promo.usecase.promo;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import uk.co.mruoc.promo.entity.promo.Promo;
import uk.co.mruoc.promo.entity.promo.PromoFactory;


@Builder
@Slf4j
public class PromoPopulator implements ApplicationListener<ContextRefreshedEvent>, Ordered {

    private final PromoFactory factory;
    private final PromoService service;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        var promo = factory.buildFreeBurgers();
        createIfRequired(promo);
    }

    @Override
    public int getOrder() {
        return 10;
    }

    private void createIfRequired(Promo promo) {
        if (service.find(promo.getId()).isEmpty()) {
            log.info("creating promo {}", promo);
            service.create(promo);
        }
    }

}
