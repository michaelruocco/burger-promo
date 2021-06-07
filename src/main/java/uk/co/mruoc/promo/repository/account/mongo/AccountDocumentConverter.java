package uk.co.mruoc.promo.repository.account.mongo;

import uk.co.mruoc.promo.entity.account.Account;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class AccountDocumentConverter {

    public Account toAccount(AccountDocument document) {
        return Account.builder()
                .id(document.getId())
                .promoClaims(toPromoClaims(document.getPromoClaims()))
                .build();
    }

    public AccountDocument toDocument(Account account) {
        return AccountDocument.builder()
                .id(account.getId())
                .promoClaims(toDocumentPromoClaims(account.getPromoClaims()))
                .build();
    }

    public Stream<AccountDocument> toDocuments(Collection<Account> accounts) {
        return accounts.stream().map(this::toDocument);
    }

    private static Map<String, AtomicLong> toPromoClaims(Map<String, Long> documentPromoClaims) {
        Map<String, AtomicLong> promoClaims = new HashMap<>();
        documentPromoClaims.forEach((key, value) -> promoClaims.put(key, new AtomicLong(value)));
        return promoClaims;
    }

    private static Map<String, Long> toDocumentPromoClaims(Map<String, AtomicLong> promoClaims) {
        Map<String, Long> documentPromoClaims = new HashMap<>();
        promoClaims.forEach((key, value) -> documentPromoClaims.put(key, value.get()));
        return documentPromoClaims;
    }

}
