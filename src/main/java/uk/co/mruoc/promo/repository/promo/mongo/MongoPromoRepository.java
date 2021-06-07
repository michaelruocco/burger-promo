package uk.co.mruoc.promo.repository.promo.mongo;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import lombok.Builder;
import org.bson.Document;
import org.bson.conversions.Bson;
import uk.co.mruoc.promo.entity.promo.Promo;
import uk.co.mruoc.promo.entity.promo.PromoClaimRequest;
import uk.co.mruoc.promo.usecase.promo.PromoRepository;

import java.time.Instant;
import java.util.Optional;

import static uk.co.mruoc.duration.logger.MongoMdcDurationLoggerUtils.logDuration;

@Builder
public class MongoPromoRepository implements PromoRepository {

    private final MongoCollection<PromoDocument> promoCollection;

    @Builder.Default
    private final PromoQueryBuilder queryBuilder = new PromoQueryBuilder();

    @Builder.Default
    private final PromoDocumentConverter promoConverter = new PromoDocumentConverter();

    @Override
    public void claim(PromoClaimRequest request) {
        var start = Instant.now();
        try {
            var query = queryBuilder.toFindByIdQuery(request.getPromoId());
            var update = new Document("$inc", new Document("totalClaims", 1));
            promoCollection.updateOne(query, update);
        } finally {
            logDuration("mongo-promo-claim", start);
        }
    }

    @Override
    public void reset(String promoId) {
        var query = queryBuilder.toFindByIdQuery(promoId);
        var update = new Document("$set", new Document("totalClaims", 0));
        promoCollection.updateOne(query, update);
    }

    @Override
    public void create(Promo updated) {
        PromoDocument document = promoConverter.toDocument(updated);
        promoCollection.insertOne(document);
    }

    @Override
    public boolean exists(String promoId) {
        Bson query = queryBuilder.toFindByIdQuery(promoId);
        return promoCollection.countDocuments(query) > 0;
    }

    @Override
    public Optional<Promo> find(String promoId) {
        var start = Instant.now();
        try {
            Bson query = queryBuilder.toFindByIdQuery(promoId);
            FindIterable<PromoDocument> documents = promoCollection.find(query);
            return Optional.ofNullable(documents.first()).map(promoConverter::toPromo);
        } finally {
            logDuration("find-promo", start);
        }
    }

}
