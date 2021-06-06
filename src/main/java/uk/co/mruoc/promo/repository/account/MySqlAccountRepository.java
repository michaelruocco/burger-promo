package uk.co.mruoc.promo.repository.account;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.promo.entity.account.Account;
import uk.co.mruoc.promo.usecase.account.AccountRepository;

import java.util.Optional;

@Builder
@Slf4j
public class MySqlAccountRepository implements AccountRepository {

    private final AccountEntityRepository accountRepository;

    @Builder.Default
    private final AccountEntityConverter converter = new AccountEntityConverter();

    @Override
    public void save(Account account) {
        AccountEntity entity = converter.toEntity(account);
        accountRepository.save(entity);
    }

    @Override
    public Optional<Account> find(String id) {
        return accountRepository.findById(id).map(converter::toAccount);
    }

}
