package uk.co.mruoc.promo.repository.mongo;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import uk.co.mruoc.promo.repository.RepositoryConfig;
import uk.co.mruoc.promo.repository.account.mongo.AccountCollection;
import uk.co.mruoc.promo.repository.account.mongo.MongoAccountRepository;
import uk.co.mruoc.promo.repository.promo.mongo.MongoPromoRepository;
import uk.co.mruoc.promo.repository.promo.mongo.PromoCollection;
import uk.co.mruoc.promo.usecase.account.AccountRepository;
import uk.co.mruoc.promo.usecase.promo.PromoRepository;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@RequiredArgsConstructor
public class MongoRepositoryConfig implements RepositoryConfig {

    private final AccountRepository accountRepository;
    private final PromoRepository promoRepository;

    public MongoRepositoryConfig() {
        this(new LocalMongo().getDatabase(buildCodecRegistry()));
    }

    public MongoRepositoryConfig(MongoDatabase database) {
        this(database, toAccountRepository(database));
    }

    public MongoRepositoryConfig(MongoDatabase database, MongoAccountRepository accountRepository) {
        this(accountRepository, toPromoRepository(database, accountRepository));
    }

    @Override
    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    @Override
    public PromoRepository getPromoRepository() {
        return promoRepository;
    }

    private static CodecRegistry buildCodecRegistry() {
        var pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        return fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
    }

    private static MongoAccountRepository toAccountRepository(MongoDatabase database) {
        return MongoAccountRepository.builder()
                .collection(AccountCollection.get(database))
                .build();
    }

    private static PromoRepository toPromoRepository(MongoDatabase database, MongoAccountRepository accountRepository) {
        return MongoPromoRepository.builder()
                .collection(PromoCollection.get(database))
                .accountRepository(accountRepository)
                .build();
    }

}
