package uk.co.mruoc.promo.repository.mysql;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.promo.repository.RepositoryConfig;
import uk.co.mruoc.promo.repository.account.mysql.MysqlAccountRepository;
import uk.co.mruoc.promo.repository.promo.mysql.MysqlPromoRepository;
import uk.co.mruoc.promo.usecase.account.AccountRepository;
import uk.co.mruoc.promo.usecase.promo.PromoRepository;

import javax.sql.DataSource;

@RequiredArgsConstructor
public class MysqlRepositoryConfig implements RepositoryConfig {

    private final MysqlAccountRepository accountRepository;
    private final MysqlPromoRepository promoRepository;

    public MysqlRepositoryConfig(DataSource dataSource) {
        this(toAccountRepository(dataSource), toPromoRepository(dataSource));
    }

    @Override
    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    @Override
    public PromoRepository getPromoRepository() {
        return promoRepository;
    }

    private static MysqlAccountRepository toAccountRepository(DataSource dataSource) {
        return new MysqlAccountRepository(dataSource);
    }

    private static MysqlPromoRepository toPromoRepository(DataSource dataSource) {
        return new MysqlPromoRepository(dataSource);
    }

}
