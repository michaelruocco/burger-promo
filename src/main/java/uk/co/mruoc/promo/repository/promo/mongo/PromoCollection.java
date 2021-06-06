package uk.co.mruoc.promo.repository.promo.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class PromoCollection {

    private static final String NAME = "promo";

    private PromoCollection() {
        // constants class
    }

    public static void create(MongoDatabase database) {
        database.createCollection(NAME);
    }

    public static MongoCollection<PromoDocument> get(MongoDatabase database) {
        return database.getCollection(NAME, PromoDocument.class);
    }

}
