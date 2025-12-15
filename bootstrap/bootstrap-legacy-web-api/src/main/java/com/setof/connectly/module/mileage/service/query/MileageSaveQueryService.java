package com.setof.connectly.module.mileage.service.query;

import com.setof.connectly.module.mileage.dto.query.PendingMileageQueryDto;

public interface MileageSaveQueryService {

    void saveOrderExpectedMileage(
            long userId, long paymentAmount, PendingMileageQueryDto pendingMileageQuery);
}
