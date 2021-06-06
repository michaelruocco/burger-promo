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

    public long getClaims(String promoId) {
        return claimedPromos.stream().filter(claimedId -> claimedId.equals(promoId)).count();
    }

    public Account claim(String promoId) {
        Collection<String> updatedClaimedPromos = new ArrayList<>(claimedPromos);
        updatedClaimedPromos.add(promoId);
        return toBuilder().claimedPromos(updatedClaimedPromos).build();
    }

}
