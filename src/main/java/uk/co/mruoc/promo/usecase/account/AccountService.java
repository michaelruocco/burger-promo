package uk.co.mruoc.promo.usecase.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.promo.entity.account.Account;
import uk.co.mruoc.promo.entity.account.AccountNotFoundException;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository repository;

    public Account forceFind(String id) {
        return find(id).orElseThrow(() -> new AccountNotFoundException(id));
    }

    public Optional<Account> find(String id) {
        return repository.find(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void saveAll(Collection<Account> accounts) {
        repository.saveAll(accounts);
    }

    public void save(Account account) {
        repository.save(account);
    }

}
