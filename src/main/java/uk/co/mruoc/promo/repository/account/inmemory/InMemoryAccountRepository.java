package uk.co.mruoc.promo.repository.account.inmemory;

import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.promo.entity.account.Account;
import uk.co.mruoc.promo.usecase.account.AccountOptimisticLockException;
import uk.co.mruoc.promo.usecase.account.AccountRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Slf4j
public class InMemoryAccountRepository implements AccountRepository {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Optional<Account> find(String id) {
        return Optional.ofNullable(accounts.get(id));
    }

    @Override
    public Stream<Account> findAccountsByClaimedPromo(String promoId) {
        return accounts.values().stream().filter(account -> account.hasClaims(promoId));
    }

    @Override
    public void deleteAll() {
        accounts.clear();
    }

    @Override
    public void saveAll(Collection<Account> accountsToSave) {
        accountsToSave.forEach(this::save);
    }

    @Override
    public void save(Account updated) {
        Optional<Account> existing = find(updated.getId());
        existing.ifPresent(account -> validateUpdate(account, updated));
        accounts.put(updated.getId(), updated);
    }

    private void validateUpdate(Account existing, Account updated) {
        String id = updated.getId();
        if (existing.getVersion() != updated.getVersion() - 1) {
            throw new AccountOptimisticLockException(id, existing.getVersion(), updated.getVersion());
        }
    }

}
