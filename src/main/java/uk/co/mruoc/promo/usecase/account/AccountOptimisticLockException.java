package uk.co.mruoc.promo.usecase.account;

import uk.co.mruoc.promo.usecase.OptimisticLockException;

public class AccountOptimisticLockException extends OptimisticLockException {

    public AccountOptimisticLockException(String id, long existingVersion, long updatedVersion) {
        super(String.format("update failed for account %s existing version %d new version %d",
                id,
                existingVersion,
                updatedVersion)
        );
    }

}
