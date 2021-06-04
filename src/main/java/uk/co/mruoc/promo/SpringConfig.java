package uk.co.mruoc.promo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.mruoc.promo.repository.InMemoryPromoRepository;
import uk.co.mruoc.promo.repository.PromoRepository;
import uk.co.mruoc.promo.usecase.PromoPopulator;
import uk.co.mruoc.promo.usecase.PromoService;

@Configuration
public class SpringConfig {

    @Bean
    public PromoPopulator populator(PromoService service) {
        return new PromoPopulator(service);
    }

    @Bean
    public PromoService service(PromoRepository repository) {
        return new PromoService(repository);
    }

    @Bean
    public PromoRepository repository() {
        return new InMemoryPromoRepository();
    }

}
