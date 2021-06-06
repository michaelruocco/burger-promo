package uk.co.mruoc.promo.repository.account.mongo;

import com.mongodb.BasicDBObject;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;

public class AccountQueryBuilder {

    public Bson toFindByIdQuery(String accountId) {
        return eq("_id", accountId);
    }

    public Bson toFindByClaimedPromoQuery(String promoId) {
        return in("claimedPromos", promoId);
    }

    public Bson all() {
        return new BasicDBObject();
    }

}
