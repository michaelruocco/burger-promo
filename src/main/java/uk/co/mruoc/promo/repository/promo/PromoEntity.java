package uk.co.mruoc.promo.repository.promo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PromoEntity {

    @Id
    private String id;
    private long totalAllowedClaims;
    private long claimsAllowedPerAccount;
    private long totalClaims;
    @Version
    private long version;

}
