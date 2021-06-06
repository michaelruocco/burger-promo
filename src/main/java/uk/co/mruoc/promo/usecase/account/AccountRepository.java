package uk.co.mruoc.promo.usecase.account;

import uk.co.mruoc.promo.entity.account.Account;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public interface AccountRepository {

    Optional<Account> find(String id);

    Stream<Account> findAccountsByClaimedPromo(String promoId);

    void deleteAll();

    void saveAll(Collection<Account> accounts);

    void save(Account account);

}
