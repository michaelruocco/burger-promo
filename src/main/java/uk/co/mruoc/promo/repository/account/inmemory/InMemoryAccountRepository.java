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
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class InMemoryAccountRepository implements AccountRepository {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    public AtomicLong findClaimsCount(PromoClaimRequest request) {
        return find(request.getAccountId())
                .map(account -> account.getClaimsCount(request.getPromoId()))
                .orElse(new AtomicLong());
    }

    public void claim(PromoClaimRequest request) {
        var accountId = request.getAccountId();
        var account = find(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
        account.claim(request.getPromoId());
    }

    public void resetPromo(String promoId) {
        accounts.values().stream()
                .filter(account -> account.hasClaims(promoId))
                .forEach(account -> account.resetClaims(promoId));
    }

    @Override
    public Optional<Account> find(String id) {
        return Optional.ofNullable(accounts.get(id));
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

}
