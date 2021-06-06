package uk.co.mruoc.promo.entity.account;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Builder(toBuilder = true)
@Data
public class Account {

    private final String id;

    @Builder.Default
    private final Collection<String> claimedPromos = Collections.emptyList();

    @Builder.Default
    private final long version = 0;

    public boolean hasClaims(String promoId) {
        return claimedPromos.stream().anyMatch(claimedId -> claimedId.equals(promoId));
    }

    public long getClaims(String promoId) {
        return claimedPromos.stream().filter(claimedId -> claimedId.equals(promoId)).count();
    }

    public Account removeClaimsFor(String promoId) {
        Collection<String> updatedClaimedPromos = new ArrayList<>(claimedPromos);
        updatedClaimedPromos.remove(promoId);
        return update(updatedClaimedPromos);
    }

    public Account claim(String promoId) {
        Collection<String> updatedClaimedPromos = new ArrayList<>(claimedPromos);
        updatedClaimedPromos.add(promoId);
        return update(updatedClaimedPromos);
    }

    private Account update(Collection<String> updatedClaimedPromos) {
        return toBuilder()
                .claimedPromos(updatedClaimedPromos)
                .version(calculateNextVersion())
                .build();
    }

    private long calculateNextVersion() {
        return version + 1;
    }

}
