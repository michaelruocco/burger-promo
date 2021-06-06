package uk.co.mruoc.promo.usecase.promo;

import uk.co.mruoc.promo.usecase.OptimisticLockException;

public class PromoOptimisticLockException extends OptimisticLockException {

    public PromoOptimisticLockException(String id, long existingVersion, long updatedVersion) {
        super(String.format("update failed for promo %s existing version %d new version %d",
                id,
                existingVersion,
                updatedVersion)
        );
    }

}
