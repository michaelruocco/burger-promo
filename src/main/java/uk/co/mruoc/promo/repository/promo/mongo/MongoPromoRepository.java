package uk.co.mruoc.promo.repository.promo.mongo;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import uk.co.mruoc.promo.entity.promo.Promo;
import uk.co.mruoc.promo.entity.promo.PromoAvailability;
import uk.co.mruoc.promo.entity.promo.PromoClaimRequest;
import uk.co.mruoc.promo.repository.account.mongo.MongoAccountRepository;
import uk.co.mruoc.promo.usecase.promo.PromoRepository;

import java.util.Optional;

@Slf4j
@Builder
public class MongoPromoRepository implements PromoRepository {

    private final MongoCollection<PromoDocument> collection;
    private final MongoAccountRepository accountRepository;

    @Builder.Default
    private final PromoQueryBuilder queryBuilder = new PromoQueryBuilder();

    @Builder.Default
    private final PromoDocumentConverter promoConverter = new PromoDocumentConverter();

    @Override
    public void claim(PromoClaimRequest request) {
        var query = queryBuilder.toFindByIdQuery(request.getPromoId());
        var update = new Document("$inc", new Document("totalClaims", 1));
        collection.updateOne(query, update);
        accountRepository.claim(request);
    }

    @Override
    public void reset(String promoId) {
        var query = queryBuilder.toFindByIdQuery(promoId);
        var update = new Document("$set", new Document("totalClaims", 0));
        collection.updateOne(query, update);
        accountRepository.resetClaims(promoId);
    }

    @Override
    public void create(Promo updated) {
        PromoDocument document = promoConverter.toDocument(updated);
        collection.insertOne(document);
    }

    @Override
    public boolean exists(String promoId) {
        Bson query = queryBuilder.toFindByIdQuery(promoId);
        return collection.countDocuments(query) > 0;
    }

    @Override
    public void delete(String promoId) {
        Bson query = queryBuilder.toFindByIdQuery(promoId);
        collection.deleteOne(query);
    }

    @Override
    public Optional<PromoAvailability> findAvailability(PromoClaimRequest request) {
        var promo = find(request.getPromoId());
        return promo.map(value -> PromoAvailability.builder()
                .promo(value)
                .accountId(request.getAccountId())
                .accountClaims(accountRepository.findClaimsCount(request))
                .build());
    }

    @Override
    public Optional<Promo> find(String promoId) {
        Bson query = queryBuilder.toFindByIdQuery(promoId);
        FindIterable<PromoDocument> documents = collection.find(query);
        return Optional.ofNullable(documents.first()).map(promoConverter::toPromo);
    }

}
