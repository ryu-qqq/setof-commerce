package com.setof.connectly.module.mileage.service.query;

import com.setof.connectly.module.order.dto.query.RefundOrderInfo;
import com.setof.connectly.module.user.entity.Users;

public interface MileageQueryService {

    void saveMileageForJoining(Users users);

    void rollBackMileage(long paymentId, long userId, RefundOrderInfo refundOrderInfo);

    void deductMileage(long paymentId, long userId, long paymentAmount, double usedMileageAmount);
}
