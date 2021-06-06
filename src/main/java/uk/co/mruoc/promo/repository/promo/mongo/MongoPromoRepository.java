package uk.co.mruoc.promo.repository.promo.mongo;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Builder;
import org.bson.conversions.Bson;
import uk.co.mruoc.promo.entity.promo.Promo;
import uk.co.mruoc.promo.usecase.promo.PromoOptimisticLockException;
import uk.co.mruoc.promo.usecase.promo.PromoRepository;

import java.time.Instant;
import java.util.Optional;

import static uk.co.mruoc.duration.logger.MongoMdcDurationLoggerUtils.logDuration;

@Builder
public class MongoPromoRepository implements PromoRepository {

    private final MongoCollection<PromoDocument> collection;

    @Builder.Default
    private final PromoQueryBuilder queryBuilder = new PromoQueryBuilder();

    @Builder.Default
    private final PromoDocumentConverter promoConverter = new PromoDocumentConverter();

    @Override
    public void save(Promo updated) {
        var start = Instant.now();
        try {
            Optional<Promo> existing = find(updated.getId());
            existing.ifPresent(promo -> validateUpdate(promo, updated));
            PromoDocument document = promoConverter.toDocument(updated);
            var query = queryBuilder.toFindByIdQuery(updated.getId());
            var options = new ReplaceOptions().upsert(true);
            collection.replaceOne(query, document, options);
        } finally {
            logDuration("save-promo", start);
        }
    }

    @Override
    public boolean exists(String promoId) {
        var start = Instant.now();
        try {
            Bson query = queryBuilder.toFindByIdQuery(promoId);
            return collection.countDocuments(query) > 0;
        } finally {
            logDuration("promo-exists-by-id", start);
        }
    }

    @Override
    public Optional<Promo> find(String promoId) {
        var start = Instant.now();
        try {
            Bson query = queryBuilder.toFindByIdQuery(promoId);
            FindIterable<PromoDocument> documents = collection.find(query);
            return Optional.ofNullable(documents.first()).map(promoConverter::toPromo);
        } finally {
            logDuration("find-promo-by-id", start);
        }
    }

    private void validateUpdate(Promo existing, Promo updated) {
        String id = updated.getId();
        if (existing.getVersion() != updated.getVersion() - 1) {
            throw new PromoOptimisticLockException(id, existing.getVersion(), updated.getVersion());
        }
    }

}
