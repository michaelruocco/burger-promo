package uk.co.mruoc.promo.repository.account.mysql;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.promo.entity.account.Account;
import uk.co.mruoc.promo.usecase.account.AccountRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import static uk.co.mruoc.duration.calculator.DurationCalculatorUtils.millisBetweenNowAnd;

@Builder
@Slf4j
public class MySqlAccountRepository implements AccountRepository {

    private final AccountEntityRepository accountRepository;

    @Builder.Default
    private final AccountEntityConverter converter = new AccountEntityConverter();

    @Override
    public void saveAll(Collection<Account> accounts) {
        var start = Instant.now();
        try {
            Collection<AccountEntity> entities = converter.toEntities(accounts);
            accountRepository.saveAll(entities);
        } finally {
            log.info("save all accounts took {}ms to complete", millisBetweenNowAnd(start));
        }
    }

    @Override
    public void save(Account account) {
        var start = Instant.now();
        try {
            AccountEntity entity = converter.toEntity(account);
            accountRepository.save(entity);
        } finally {
            log.info("save account took {}ms to complete", millisBetweenNowAnd(start));
        }
    }

    @Override
    public Optional<Account> find(String id) {
        var start = Instant.now();
        try {
            return accountRepository.findById(id).map(converter::toAccount);
        } finally {
            log.info("find account took {}ms to complete", millisBetweenNowAnd(start));
        }
    }

    @Override
    public void deleteAll() {
        var start = Instant.now();
        try {
            accountRepository.deleteAll();
        } finally {
            log.info("delete all accounts took {}ms to complete", millisBetweenNowAnd(start));
        }
    }

}
