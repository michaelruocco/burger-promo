package uk.co.mruoc.promo.usecase.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.promo.entity.account.Account;
import uk.co.mruoc.promo.entity.account.AccountNotFoundException;
import uk.co.mruoc.promo.entity.promo.PromoClaimRequest;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository repository;

    public Account forceFind(String id) {
        return find(id).orElseThrow(() -> new AccountNotFoundException(id));
    }

    public Stream<Account> findWithClaims(String promoId) {
        return repository.findAccountsWithClaimedPromo(promoId);
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

    public void claim(PromoClaimRequest request) {
        repository.claim(request);
    }

    public void resetAccountClaims(String promoId) {
        repository.findAccountsWithClaimedPromo(promoId)
                .forEach(account -> repository.resetClaims(account.getId(), promoId));
    }
}
