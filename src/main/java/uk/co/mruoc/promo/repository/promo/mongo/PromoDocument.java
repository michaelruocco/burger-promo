package uk.co.mruoc.promo.repository.promo.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonId;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromoDocument {

    @BsonId
    private String id;
    private long totalAllowedClaims;
    private long claimsAllowedPerAccount;
    private long totalClaims;

}
