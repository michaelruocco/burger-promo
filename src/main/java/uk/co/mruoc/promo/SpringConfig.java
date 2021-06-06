package uk.co.mruoc.promo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.mruoc.promo.entity.account.AccountFactory;
import uk.co.mruoc.promo.entity.promo.PromoFactory;
import uk.co.mruoc.promo.repository.account.inmemory.InMemoryAccountRepository;
import uk.co.mruoc.promo.repository.promo.inmemory.InMemoryPromoRepository;
import uk.co.mruoc.promo.usecase.account.AccountPopulator;
import uk.co.mruoc.promo.usecase.account.AccountRepository;
import uk.co.mruoc.promo.usecase.account.AccountService;
import uk.co.mruoc.promo.usecase.PromoClaimService;
import uk.co.mruoc.promo.usecase.PromoFacade;
import uk.co.mruoc.promo.usecase.promo.PromoRepository;
import uk.co.mruoc.promo.usecase.promo.PromoPopulator;
import uk.co.mruoc.promo.usecase.promo.PromoService;

@Configuration
public class SpringConfig {

    @Bean
    public AccountFactory accountFactory() {
        return new AccountFactory();
    }

    @Bean
    public PromoFactory promoFactory() {
        return new PromoFactory();
    }

    @Bean
    public AccountPopulator accountPopulator(AccountFactory factory, AccountService service) {
        return AccountPopulator.builder()
                .factory(factory)
                .service(service)
                .build();
    }

    @Bean
    public PromoPopulator promoPopulator(PromoFactory factory, PromoService service) {
        return PromoPopulator.builder()
                .factory(factory)
                .service(service)
                .build();
    }

    @Bean
    public PromoFacade promoFacade(PromoClaimService claimService,
                                   PromoService promoService) {
        return PromoFacade.builder()
                .claimService(claimService)
                .promoService(promoService)
                .build();
    }

    @Bean
    public PromoClaimService promoClaimService(AccountService accountService,
                                               PromoService promoService) {
        return PromoClaimService.builder()
                .accountService(accountService)
                .promoService(promoService)
                .build();
    }

    @Bean
    public AccountService accountService(AccountRepository repository) {
        return new AccountService(repository);
    }

    @Bean
    public PromoService promoService(PromoRepository repository) {
        return PromoService.builder()
                .repository(repository)
                .build();
    }

    @Bean
    public AccountRepository inMemoryAccountRepository() {
        return new InMemoryAccountRepository();
    }

    @Bean
    public PromoRepository inMemoryPromoRepository() {
        return new InMemoryPromoRepository();
    }

}
