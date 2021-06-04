package uk.co.mruoc.promo.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import uk.co.mruoc.promo.entity.Promotion;

@RequiredArgsConstructor
public class PromoPopulator implements ApplicationListener<ContextRefreshedEvent> {

    private final PromoService service;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        service.create(new Promotion());
    }
}
