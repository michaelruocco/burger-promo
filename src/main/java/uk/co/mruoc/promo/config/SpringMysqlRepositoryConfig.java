package uk.co.mruoc.promo.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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
import java.time.Duration;
import java.util.Optional;

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

    @Bean
    public DataSource mysqlDataSource() {
        var config = new HikariConfig();
        config.setJdbcUrl(loadMysqlJdbcUrl());
        config.setUsername(loadMysqlUsername());
        config.setPassword(loadMysqlPassword());
        config.setMaximumPoolSize(50);
        config.setConnectionTimeout(2500);
        return new HikariDataSource(config);
    }

    private static String loadMysqlJdbcUrl() {
        return Optional.ofNullable(System.getProperty("spring.datasource.url"))
                .orElseThrow(() -> new IllegalStateException("spring.datasource.url not configured"));
    }

    private static String loadMysqlUsername() {
        return Optional.ofNullable(System.getProperty("spring.datasource.username"))
                .orElseThrow(() -> new IllegalStateException("spring.datasource.username not configured"));
    }

    private static String loadMysqlPassword() {
        return Optional.ofNullable(System.getProperty("spring.datasource.password"))
                .orElseThrow(() -> new IllegalStateException("spring.datasource.password not configured"));
    }

}
