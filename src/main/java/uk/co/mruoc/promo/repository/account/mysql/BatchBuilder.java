package uk.co.mruoc.promo.repository.account.mysql;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import uk.co.mruoc.promo.entity.account.Account;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class BatchBuilder {

    private static final int DEFAULT_BATCH_SIZE = 1000;

    private final int batchSize;

    public BatchBuilder() {
        this(DEFAULT_BATCH_SIZE);
    }

    public List<List<Account>> toBatches(Collection<Account> accounts) {
        return ListUtils.partition(new ArrayList<>(accounts), batchSize);
    }

}
