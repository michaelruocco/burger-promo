package uk.co.mruoc.promo.repository.account.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonId;

import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDocument {

    @BsonId
    private String id;
    private Map<String, Long> promoClaims;

}
