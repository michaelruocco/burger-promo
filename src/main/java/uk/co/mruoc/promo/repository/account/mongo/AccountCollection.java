package uk.co.mruoc.promo.repository.account.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import org.bson.conversions.Bson;

public class AccountCollection {

    public static final String CLAIMED_PROMOS_INDEX_NAME = "claimedPromos";

    private static final String NAME = "account";

    private AccountCollection() {
        // constants class
    }

    public static void create(MongoDatabase database) {
        database.createCollection(NAME);
        database.getCollection(NAME).createIndex(indexKeys());
    }

    public static MongoCollection<AccountDocument> get(MongoDatabase database) {
        return database.getCollection(NAME, AccountDocument.class);
    }

    private static Bson indexKeys() {
        return Indexes.ascending(CLAIMED_PROMOS_INDEX_NAME);
    }

}
