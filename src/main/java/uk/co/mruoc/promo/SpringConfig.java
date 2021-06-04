package uk.co.mruoc.promo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.mruoc.promo.entity.PromoFactory;
import uk.co.mruoc.promo.repository.MySqlPromoRepository;
import uk.co.mruoc.promo.repository.PromoRepository;
import uk.co.mruoc.promo.usecase.PromoPopulator;
import uk.co.mruoc.promo.usecase.PromoService;

import javax.persistence.EntityManager;

@Configuration
public class SpringConfig {

    @Bean
    public PromoPopulator populator(PromoFactory factory, PromoService service) {
        return PromoPopulator.builder()
                .factory(factory)
                .service(service)
                .build();
    }

    @Bean
    public PromoFactory factory() {
        return new PromoFactory();
    }

    @Bean
    public PromoService service(PromoRepository repository) {
        return new PromoService(repository);
    }

    @Bean
    public PromoRepository repository(EntityManager entityManager) {
        return new MySqlPromoRepository(entityManager);
    }

}
