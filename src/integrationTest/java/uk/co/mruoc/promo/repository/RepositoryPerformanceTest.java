package uk.co.mruoc.promo.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import uk.co.mruoc.promo.entity.account.AccountFactory;
import uk.co.mruoc.promo.entity.promo.Promo;
import uk.co.mruoc.promo.entity.promo.PromoClaimRequest;
import uk.co.mruoc.promo.entity.promo.PromoClaimRequest.PromoClaimRequestBuilder;
import uk.co.mruoc.promo.entity.promo.PromoFactory;
import uk.co.mruoc.promo.repository.mongo.LocalDockerMongo;
import uk.co.mruoc.promo.repository.mongo.MongoRepositoryConfig;
import uk.co.mruoc.promo.repository.mysql.LocalDockerMysql;
import uk.co.mruoc.promo.repository.mysql.MysqlRepositoryConfig;
import uk.co.mruoc.promo.repository.stubbed.StubbedRepositoryConfig;
import uk.co.mruoc.promo.usecase.account.AccountRepository;
import uk.co.mruoc.promo.usecase.promo.PromoRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@Slf4j
class RepositoryPerformanceTest {

    private static final String PROMO_ID = "free-burgers";
    private static final PromoClaimRequestBuilder REQUEST_BUILDER = PromoClaimRequest.builder().promoId(PROMO_ID);
    private static final int NUMBER_OF_ACCOUNTS = 1000;

    @Container
    private static final LocalDockerMysql mysql = new LocalDockerMysql();

    @Container
    private static final LocalDockerMongo mongo = new LocalDockerMongo();

    @ParameterizedTest
    @MethodSource("provideRepositoryConfigs")
    void performanceTest(RepositoryConfig config) throws InterruptedException {
        var promoRepository = config.getPromoRepository();
        populateAccounts(config.getAccountRepository());
        populatePromo(promoRepository);
        warmup(config);

        var latch = new CountDownLatch(NUMBER_OF_ACCOUNTS);
        var readStats = new Stats();
        var updateStats = new Stats();
        ClaimRunnable.ClaimRunnableBuilder runnableBuilder = ClaimRunnable.builder()
                .latch(latch)
                .promoRepository(config.getPromoRepository())
                .readStats(readStats)
                .updateStats(updateStats);

        var executorService = Executors.newFixedThreadPool(100);
        log.info("running test...");
        var start = Instant.now();
        try {
            IntStream.rangeClosed(1, NUMBER_OF_ACCOUNTS)
                    .mapToObj(Integer::toString)
                    .map(accountId -> runnableBuilder.request(toRequest(accountId)).build())
                    .forEach(executorService::submit);
            latch.await();
        } finally {
            log.info("promo took {}ms", Duration.between(start, Instant.now()).toMillis());
            executorService.shutdown();

            log.info("read{}", readStats.getMessage());
            log.info("update{}", updateStats.getMessage());

            var promo = promoRepository.find(PROMO_ID);
            log.info("final promo {}", promo);
            assertThat(promo)
                    .map(Promo::getTotalClaims)
                    .map(AtomicLong::get)
                    .contains((long) NUMBER_OF_ACCOUNTS);
        }
    }

    private static Stream<Arguments> provideRepositoryConfigs() {
        return Stream.of(
                Arguments.of(new StubbedRepositoryConfig()),
                Arguments.of(new MysqlRepositoryConfig(mysql.getDataSource())),
                Arguments.of(new MongoRepositoryConfig(mongo.getDatabase(MongoRepositoryConfig.buildCodecRegistry())))
        );
    }

    private static void warmup(RepositoryConfig config) {
        log.info("running warmup requests...");
        PromoClaimRequest request = toRequest("1");
        warmupPromo(config.getPromoRepository(), request);
        warmupReset(config.getPromoRepository(), request);
    }

    private static void warmupPromo(PromoRepository promoRepository, PromoClaimRequest request) {
        promoRepository.find(request.getPromoId());
        promoRepository.claim(request);
    }

    private static void warmupReset(PromoRepository promoRepository, PromoClaimRequest request) {
        String promoId = request.getPromoId();
        promoRepository.reset(promoId);
    }

    private static void populateAccounts(AccountRepository accountRepository) {
        var factory = new AccountFactory();
        log.info("populating {} accounts...", NUMBER_OF_ACCOUNTS);
        accountRepository.deleteAll();
        accountRepository.saveAll(factory.buildAccounts(NUMBER_OF_ACCOUNTS));
        log.info("{} accounts populated", NUMBER_OF_ACCOUNTS);
    }

    private static void populatePromo(PromoRepository promoRepository) {
        var promo = new PromoFactory().buildFreeBurgers();
        promoRepository.delete(promo.getId());
        promoRepository.create(promo);
        log.info("deleted and recreated promo {}", promo);
    }

    private static PromoClaimRequest toRequest(String accountId) {
        return REQUEST_BUILDER.accountId(accountId).build();
    }

}
