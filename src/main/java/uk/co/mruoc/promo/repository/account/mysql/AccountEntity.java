package uk.co.mruoc.promo.repository.account.mysql;

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
public class AccountEntity {

    @Id
    private String id;

    private String claimedPromos;
//    @ElementCollection
//    private Collection<String> claimedPromos;

    @Version
    private long version;

}
