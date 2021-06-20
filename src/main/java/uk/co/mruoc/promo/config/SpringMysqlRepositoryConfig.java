package uk.co.mruoc.promo.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import uk.co.mruoc.promo.repository.account.mysql.MysqlAccountRepository;
import uk.co.mruoc.promo.repository.promo.mysql.MysqlPromoRepository;
import uk.co.mruoc.promo.usecase.account.AccountRepository;
import uk.co.mruoc.promo.usecase.promo.PromoRepository;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration(exclude = {
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class
})
@Profile("!stubbed & !mongo")
public class SpringMysqlRepositoryConfig {

    @Bean
    public AccountRepository mysqlAccountRepository(DataSource dataSource) {
        return new MysqlAccountRepository(dataSource);
    }

    @Bean
    public PromoRepository mysqlPromoRepository(DataSource dataSource) {
        return new MysqlPromoRepository(dataSource);
    }

}
