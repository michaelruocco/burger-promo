package uk.co.mruoc.promo.entity;

import com.google.common.collect.Sets;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
@Data
public class Promotion {

    private final String id;
    private final long totalAllowed;
    private final Set<String> claimedAccountIds;

    public Promotion() {
        this("free-burgers", 6000000, Sets.newConcurrentHashSet());
    }

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

    public void claim(String accountId) {
        if (anyRemaining(accountId)) {
            claimedAccountIds.add(accountId);
        }
    }

}
