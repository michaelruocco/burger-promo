package uk.co.mruoc.promo.repository.account.mysql;

import uk.co.mruoc.promo.entity.account.Account;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class AccountEntityConverter {

    public Collection<AccountEntity> toEntities(Collection<Account> accounts) {
        return accounts.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public AccountEntity toEntity(Account account) {
        return new AccountEntity(
                account.getId(),
                String.join(",", account.getClaimedPromos()),
                account.getVersion()
        );
    }

    public Account toAccount(AccountEntity entity) {
        return Account.builder()
                .id(entity.getId())
                .claimedPromos(Arrays.asList(entity.getClaimedPromos().split(",")))
                .version(entity.getVersion())
                .build();
    }

}
