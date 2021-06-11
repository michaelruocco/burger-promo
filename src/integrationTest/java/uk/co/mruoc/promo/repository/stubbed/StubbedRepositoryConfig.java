package uk.co.mruoc.promo.repository.stubbed;

import uk.co.mruoc.promo.repository.RepositoryConfig;
import uk.co.mruoc.promo.repository.account.inmemory.InMemoryAccountRepository;
import uk.co.mruoc.promo.repository.promo.inmemory.InMemoryPromoRepository;
import uk.co.mruoc.promo.usecase.account.AccountRepository;
import uk.co.mruoc.promo.usecase.promo.PromoRepository;

public class StubbedRepositoryConfig implements RepositoryConfig {

    private final InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();
    private final PromoRepository promoRepository = new InMemoryPromoRepository(accountRepository);

    @Override
    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    @Override
    public PromoRepository getPromoRepository() {
        return promoRepository;
    }

}
