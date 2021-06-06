package uk.co.mruoc.promo.config;

import com.github.mongobee.Mongobee;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import uk.co.mruoc.promo.repository.account.mongo.AccountCollection;
import uk.co.mruoc.promo.repository.account.mongo.AccountMongobeeChangelog;
import uk.co.mruoc.promo.repository.account.mongo.MongoAccountRepository;
import uk.co.mruoc.promo.repository.promo.mongo.MongoPromoRepository;
import uk.co.mruoc.promo.repository.promo.mongo.PromoCollection;
import uk.co.mruoc.promo.repository.promo.mongo.PromoMongobeeChangelog;
import uk.co.mruoc.promo.usecase.account.AccountRepository;
import uk.co.mruoc.promo.usecase.promo.PromoRepository;

import static java.util.Objects.requireNonNull;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
@Profile("!stubbed")
public class SpringMongoRepositoryConfig {

    private static final ConnectionString CONNECTION_STRING = loadConnectionString();

    @Bean
    public MongoClient mongoClient() {
        var pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        var codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(CONNECTION_STRING)
                .codecRegistry(codecRegistry)
                .build();
        return MongoClients.create(settings);
    }

    @Bean
    public MongoDatabase mongoDatabase(MongoClient client) {
        return client.getDatabase(requireNonNull(CONNECTION_STRING.getDatabase()));
    }

    @Bean
    public Mongobee accountMongobee() {
        return toMongobee(AccountMongobeeChangelog.class.getPackageName());
    }

    @Bean
    public Mongobee promoMongobee() {
        return toMongobee(PromoMongobeeChangelog.class.getPackageName());
    }

    @Bean
    public AccountRepository mongoAccountRepository(MongoDatabase database) {
        return MongoAccountRepository.builder()
                .collection(AccountCollection.get(database))
                .build();
    }

    @Bean
    public PromoRepository mongoPromoRepository(MongoDatabase database) {
        return MongoPromoRepository.builder()
                .collection(PromoCollection.get(database))
                .build();
    }

    private static Mongobee toMongobee(String changeLogPackageName) {
        var runner = new Mongobee(CONNECTION_STRING.getConnectionString());
        runner.setChangeLogsScanPackage(changeLogPackageName);
        return runner;
    }

    private static ConnectionString loadConnectionString() {
        return new ConnectionString(System.getProperty("spring.data.mongodb.uri"));
    }

}
