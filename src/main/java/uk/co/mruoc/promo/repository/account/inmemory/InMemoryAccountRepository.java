package uk.co.mruoc.promo.repository.account.inmemory;

import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.promo.entity.account.Account;
import uk.co.mruoc.promo.entity.account.AccountNotFoundException;
import uk.co.mruoc.promo.entity.promo.PromoClaimRequest;
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
    public void claim(PromoClaimRequest request) {
        var account = forceFind(request.getAccountId());
        account.claim(request.getPromoId());
    }

    @Override
    public void resetClaims(String accountId, String promoId) {
        var account = forceFind(accountId);
        account.resetClaims(promoId);
    }

    @Override
    public Optional<Account> find(String id) {
        return Optional.ofNullable(accounts.get(id));
    }

    @Override
    public Stream<Account> findAccountsWithClaimedPromo(String promoId) {
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

    private void save(Account updated) {
        accounts.put(updated.getId(), updated);
    }

    private Account forceFind(String accountId) {
        return find(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
    }

}
