package uk.co.mruoc.promo.usecase.account;

import uk.co.mruoc.promo.entity.account.Account;
import uk.co.mruoc.promo.entity.promo.PromoClaimRequest;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public interface AccountRepository {

    void claim(PromoClaimRequest request);

    void resetClaims(String accountId, String promoId);

    Optional<Account> find(String id);

    Stream<Account> findAccountsWithClaimedPromo(String promoId);

    void deleteAll();

    void saveAll(Collection<Account> accounts);

}
