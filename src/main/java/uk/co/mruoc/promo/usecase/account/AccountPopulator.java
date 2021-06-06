package uk.co.mruoc.promo.usecase.account;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import uk.co.mruoc.promo.entity.account.Account;
import uk.co.mruoc.promo.entity.account.AccountFactory;


@Builder
@Slf4j
public class AccountPopulator implements ApplicationListener<ContextRefreshedEvent>, Ordered {

    private static final int NUMBER_TO_BUILD = 50000;

    private final AccountFactory factory;
    private final AccountService service;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("creating {} accounts...", NUMBER_TO_BUILD);
        factory.buildAccounts(NUMBER_TO_BUILD).forEach(this::createIfRequired);
        log.info("created {} accounts", NUMBER_TO_BUILD);
    }

    @Override
    public int getOrder() {
        return 20;
    }

    private void createIfRequired(Account account) {
        if (service.find(account.getId()).isEmpty()) {
            service.save(account);
        }
    }

}
