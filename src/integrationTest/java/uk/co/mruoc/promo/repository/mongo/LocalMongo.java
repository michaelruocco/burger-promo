package uk.co.mruoc.promo.repository.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;

public class LocalMongo {

    private static final String USERNAME = "promo-user";
    private static final String PASSWORD = "welcome01";
    private static final String DATABASE = "promo-local";

    public MongoDatabase getDatabase(CodecRegistry codecRegistry) {
        var settings = MongoClientSettings.builder()
                .applyConnectionString(getConnectionString())
                .codecRegistry(codecRegistry)
                .build();
        var client = MongoClients.create(settings);
        return client.getDatabase(DATABASE);
    }

    private ConnectionString getConnectionString() {
        return new ConnectionString(String.format("mongodb://%s:%s@localhost:27017/%s", USERNAME, PASSWORD, DATABASE));
    }

}
