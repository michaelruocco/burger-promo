package uk.co.mruoc.promo.usecase.account;

import uk.co.mruoc.promo.entity.account.Account;

import java.util.Optional;

public interface AccountRepository {

    Optional<Account> find(String id);

    void save(Account account);

}
