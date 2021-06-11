package uk.co.mruoc.promo.repository;

import uk.co.mruoc.promo.usecase.account.AccountRepository;
import uk.co.mruoc.promo.usecase.promo.PromoRepository;

public interface RepositoryConfig {

    AccountRepository getAccountRepository();

    PromoRepository getPromoRepository();

}
