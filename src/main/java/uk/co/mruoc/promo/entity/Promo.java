package uk.co.mruoc.promo.entity;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
@Data
@Builder(toBuilder = true)
@Slf4j
public class Promo {

    private final String id;
    private final long totalAllowed;

    @Builder.Default
    private final Collection<String> claimedAccountIds = Collections.emptyList();

    private final long version;

    public boolean anyRemaining(String accountId) {
        long remaining = getRemaining();
        if (remaining <= 0) {
            throw new PromoFinishedException(id);
        }
        if (claimedAccountIds.contains(accountId)) {
            throw new AlreadyClaimedException(accountId);
        }
        return true;
    }

    public long getRemaining() {
        return totalAllowed - claimedAccountIds.size();
    }

    public Promo claim(String accountId) {
        if (anyRemaining(accountId)) {
            return toBuilder().claimedAccountIds(claimForAccountId(accountId)).build();
        }
        return this;
    }

    public Collection<String> claimForAccountId(String accountId) {
        Collection<String> updatedIds = new ArrayList<>(claimedAccountIds);
        updatedIds.add(accountId);
        return Collections.unmodifiableCollection(updatedIds);
    }

}
