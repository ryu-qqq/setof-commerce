package com.setof.connectly.module.mileage.service.query;

import com.setof.connectly.module.mileage.dto.*;
import com.setof.connectly.module.order.entity.snapshot.mileage.OrderSnapShotMileageDetail;
import java.util.List;

public interface MileageManageService {

    MileageDeductionResult calculateMileageDeduction(
            MileageQueryDto mileageQueryDto, double usedMileageAmount);

    MileageRefundResult calculateRefundMileages(
            MileageRefundQueryDto mileageRefundQueryDto, double expectedRefundMileage);

    List<OrderSnapShotMileageDetail> distributeUsedMileage(
            long savedOrderSnapShotMileageId,
            List<MileageDto> usedMileages,
            double usedMileageForOrder);
}
