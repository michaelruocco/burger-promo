package uk.co.mruoc.promo.repository.promo.mongo;

import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.eq;

public class PromoQueryBuilder {

    public Bson toFindByIdQuery(String id) {
        return eq("_id", id);
    }

}
