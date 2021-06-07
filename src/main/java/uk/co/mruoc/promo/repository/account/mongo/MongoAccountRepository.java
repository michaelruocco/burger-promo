package uk.co.mruoc.promo.repository.account.mongo;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Builder;
import org.bson.Document;
import org.bson.conversions.Bson;
import uk.co.mruoc.promo.entity.account.Account;
import uk.co.mruoc.promo.entity.promo.PromoClaimRequest;
import uk.co.mruoc.promo.usecase.account.AccountRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static uk.co.mruoc.duration.logger.MongoMdcDurationLoggerUtils.logDuration;

@Builder
public class MongoAccountRepository implements AccountRepository {

    private final MongoCollection<AccountDocument> collection;

    @Builder.Default
    private final AccountQueryBuilder queryBuilder = new AccountQueryBuilder();

    @Builder.Default
    private final AccountDocumentConverter accountConverter = new AccountDocumentConverter();

    @Override
    public void claim(PromoClaimRequest request) {
        var start = Instant.now();
        try {
            var query = queryBuilder.toFindByIdQuery(request.getAccountId());
            var fieldName = PromoClaimsFieldName.build(request.getPromoId());
            var update = new Document("$inc", new Document(fieldName, 1));
            collection.updateOne(query, update);
        } finally {
            var action = String.format("mongo-account-claim", request.getAccountId(), request.getPromoId());
            logDuration(action, start);
        }
    }

    @Override
    public void resetClaims(String accountId, String promoId) {
        var query = queryBuilder.toFindByIdQuery(accountId);
        var fieldName = PromoClaimsFieldName.build(promoId);
        var update = new Document("$set", new Document(fieldName, 0));
        collection.updateOne(query, update);
    }

    @Override
    public Optional<Account> find(String id) {
        var start = Instant.now();
        try {
            Bson query = queryBuilder.toFindByIdQuery(id);
            FindIterable<AccountDocument> documents = collection.find(query);
            return Optional.ofNullable(documents.first()).map(accountConverter::toAccount);
        } finally {
            logDuration("find-account", start);
        }
    }

    @Override
    public Stream<Account> findAccountsWithClaimedPromo(String promoId) {
        Bson query = queryBuilder.toFindByClaimedPromoQuery(promoId);
        FindIterable<AccountDocument> documents = collection.find(query);
        return StreamSupport.stream(documents.spliterator(), false)
                .map(accountConverter::toAccount);
    }

    @Override
    public void deleteAll() {
        collection.deleteMany(queryBuilder.all());
    }

    @Override
    public void saveAll(Collection<Account> accounts) {
        if (accounts.isEmpty()) {
            return;
        }
        List<ReplaceOneModel<AccountDocument>> replacements = accountConverter.toDocuments(accounts)
                .map(this::toReplaceOneModel)
                .collect(Collectors.toList());
        collection.bulkWrite(replacements);
    }

    private ReplaceOneModel<AccountDocument> toReplaceOneModel(AccountDocument document) {
        Bson query = queryBuilder.toFindByIdQuery(document.getId());
        var options = new ReplaceOptions().upsert(true);
        return new ReplaceOneModel<>(query, document, options);
    }

}
