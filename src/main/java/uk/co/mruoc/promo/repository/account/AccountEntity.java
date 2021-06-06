package uk.co.mruoc.promo.repository.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import java.util.Collection;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {

    @Id
    private String id;

    @ElementCollection
    private Collection<String> claimedPromos;

    @Version
    private long version;

}
