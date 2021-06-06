package uk.co.mruoc.promo.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import uk.co.mruoc.promo.repository.account.inmemory.InMemoryAccountRepository;
import uk.co.mruoc.promo.repository.promo.inmemory.InMemoryPromoRepository;
import uk.co.mruoc.promo.usecase.account.AccountRepository;
import uk.co.mruoc.promo.usecase.promo.PromoRepository;

@Configuration
@EnableAutoConfiguration(exclude = {
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class
})
@Profile("stubbed")
public class SpringStubbedRepositoryConfig {

    @Bean
    public AccountRepository inMemoryAccountRepository() {
        return new InMemoryAccountRepository();
    }

    @Bean
    public PromoRepository inMemoryPromoRepository() {
        return new InMemoryPromoRepository();
    }

}
