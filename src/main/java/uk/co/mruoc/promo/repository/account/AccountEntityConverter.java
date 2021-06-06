package uk.co.mruoc.promo.repository.account;

import uk.co.mruoc.promo.entity.account.Account;

public class AccountEntityConverter {

    public AccountEntity toEntity(Account account) {
        return new AccountEntity(
                account.getId(),
                account.getClaimedPromos(),
                account.getVersion()
        );
    }

    public Account toAccount(AccountEntity entity) {
        return Account.builder()
                .id(entity.getId())
                .claimedPromos(entity.getClaimedPromos())
                .version(entity.getVersion())
                .build();
    }

}
