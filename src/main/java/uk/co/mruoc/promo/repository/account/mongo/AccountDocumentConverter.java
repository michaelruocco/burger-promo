package uk.co.mruoc.promo.repository.account.mongo;

import uk.co.mruoc.promo.entity.account.Account;

import java.util.Collection;
import java.util.stream.Stream;

public class AccountDocumentConverter {

    public Account toAccount(AccountDocument document) {
        return Account.builder()
                .id(document.getId())
                .claimedPromos(document.getClaimedPromos())
                .version(document.getVersion())
                .build();
    }

    public AccountDocument toDocument(Account account) {
        return AccountDocument.builder()
                .id(account.getId())
                .claimedPromos(account.getClaimedPromos())
                .version(account.getVersion())
                .build();
    }

    public Stream<AccountDocument> toDocuments(Collection<Account> accounts) {
        return accounts.stream().map(this::toDocument);
    }
}
