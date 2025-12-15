package com.setof.connectly.module.mileage.service.fetch;

import com.setof.connectly.module.mileage.dto.MileageQueryDto;
import com.setof.connectly.module.mileage.dto.MileageRefundQueryDto;
import com.setof.connectly.module.mileage.entity.MileageTransaction;
import java.util.Optional;

public interface MileageFindService {

    Optional<MileageQueryDto> fetchUsableMileage(long userId);

    MileageRefundQueryDto fetchUsedMileage(long paymentId, long orderId, long userId);

    Optional<MileageTransaction> fetchMileageTransactionEntity(long orderId);
}
