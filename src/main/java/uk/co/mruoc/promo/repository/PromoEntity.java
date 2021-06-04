package uk.co.mruoc.promo.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Version;
import java.util.Collection;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PromoEntity {

    @Id
    private String id;
    private long totalAllowed;
    @ElementCollection(fetch = FetchType.EAGER)
    private Collection<String> claimedAccountIds;
    @Version
    private long version;

}
