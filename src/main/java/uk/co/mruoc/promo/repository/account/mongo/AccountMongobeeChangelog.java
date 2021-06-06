package uk.co.mruoc.promo.repository.account.mongo;

import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.client.MongoDatabase;

public class AccountMongobeeChangelog {

    @ChangeSet(order = "001", id = "create-account-collection", author = "system")
    public void createAccountCollection(MongoDatabase database) {
        AccountCollection.create(database);
    }

}
