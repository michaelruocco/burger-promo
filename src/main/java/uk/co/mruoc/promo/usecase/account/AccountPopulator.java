package uk.co.mruoc.promo.usecase.account;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import uk.co.mruoc.promo.entity.account.AccountFactory;


@Builder
@Slf4j
public class AccountPopulator implements ApplicationListener<ContextRefreshedEvent>, Ordered {

    private static final int NUMBER_TO_BUILD = 6000500;

    private final AccountFactory factory;
    private final AccountService service;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("clearing accounts...");
        service.deleteAll();
        log.info("creating {} accounts...", NUMBER_TO_BUILD);
        service.saveAll(factory.buildAccounts(NUMBER_TO_BUILD));
        log.info("created {} accounts", NUMBER_TO_BUILD);
    }

    @Override
    public int getOrder() {
        return 20;
    }

}
