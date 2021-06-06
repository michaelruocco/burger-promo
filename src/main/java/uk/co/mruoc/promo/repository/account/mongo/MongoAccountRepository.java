package uk.co.mruoc.promo.repository.account.mongo;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Builder;
import org.bson.conversions.Bson;
import uk.co.mruoc.promo.entity.account.Account;
import uk.co.mruoc.promo.usecase.account.AccountOptimisticLockException;
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
    public Optional<Account> find(String id) {
        var start = Instant.now();
        try {
            Bson query = queryBuilder.toFindByIdQuery(id);
            FindIterable<AccountDocument> documents = collection.find(query);
            return Optional.ofNullable(documents.first()).map(accountConverter::toAccount);
        } finally {
            logDuration("find-account-by-id", start);
        }
    }

    @Override
    public Stream<Account> findAccountsByClaimedPromo(String promoId) {
        var start = Instant.now();
        try {
            Bson query = queryBuilder.toFindByClaimedPromoQuery(promoId);
            FindIterable<AccountDocument> documents = collection.find(query);
            return StreamSupport.stream(documents.spliterator(), false)
                    .map(accountConverter::toAccount);
        } finally {
            logDuration("find-account-by-id", start);
        }
    }

    @Override
    public void deleteAll() {
        var start = Instant.now();
        try {
            collection.deleteMany(queryBuilder.all());
        } finally {
            logDuration("delete-all-accounts", start);
        }
    }

    @Override
    public void saveAll(Collection<Account> accounts) {
        var start = Instant.now();
        try {
            List<ReplaceOneModel<AccountDocument>> replacements = accountConverter.toDocuments(accounts)
                    .map(this::toReplaceOneModel)
                    .collect(Collectors.toList());
            collection.bulkWrite(replacements);
        } finally {
            logDuration("insert-many-accounts", start);
        }
    }

    @Override
    public void save(Account updated) {
        var start = Instant.now();
        try {
            Optional<Account> existing = find(updated.getId());
            existing.ifPresent(promo -> validateUpdate(promo, updated));
            AccountDocument document = accountConverter.toDocument(updated);
            var query = queryBuilder.toFindByIdQuery(updated.getId());
            var options = new ReplaceOptions().upsert(true);
            collection.replaceOne(query, document, options);
        } finally {
            logDuration("insert-account", start);
        }
    }

    private ReplaceOneModel<AccountDocument> toReplaceOneModel(AccountDocument document) {
        Bson query = queryBuilder.toFindByIdQuery(document.getId());
        var options = new ReplaceOptions().upsert(true);
        return new ReplaceOneModel<>(query, document, options);
    }

    private void validateUpdate(Account existing, Account updated) {
        String id = updated.getId();
        if (existing.getVersion() != updated.getVersion() - 1) {
            throw new AccountOptimisticLockException(id, existing.getVersion(), updated.getVersion());
        }
    }

}
