package uk.co.mruoc.promo.repository.promo.mongo;

import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.client.MongoDatabase;

public class PromoMongobeeChangelog {

    @ChangeSet(order = "001", id = "create-promo-collection", author = "system")
    public void createPromoCollection(MongoDatabase database) {
        PromoCollection.create(database);
    }

}
