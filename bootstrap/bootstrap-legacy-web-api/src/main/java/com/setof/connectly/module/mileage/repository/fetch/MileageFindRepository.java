package com.setof.connectly.module.mileage.repository.fetch;

import com.setof.connectly.module.mileage.dto.MileageQueryDto;
import com.setof.connectly.module.mileage.dto.MileageRefundQueryDto;
import java.util.Optional;

public interface MileageFindRepository {

    Optional<MileageQueryDto> fetchMileages(long userId);

    Optional<MileageRefundQueryDto> fetchUsedMileage(long paymentId, long orderId, long userId);
}
