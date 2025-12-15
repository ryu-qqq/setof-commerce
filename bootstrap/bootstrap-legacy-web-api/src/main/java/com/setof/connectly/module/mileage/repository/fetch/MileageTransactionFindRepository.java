package com.setof.connectly.module.mileage.repository.fetch;

import com.setof.connectly.module.mileage.entity.MileageTransaction;
import java.util.Optional;

public interface MileageTransactionFindRepository {

    Optional<MileageTransaction> fetchMileageTransactionEntity(long orderId);
}
