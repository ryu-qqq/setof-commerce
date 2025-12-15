package com.setof.connectly.module.mileage.repository.query;

import com.setof.connectly.module.mileage.dto.MileageDto;
import com.setof.connectly.module.mileage.dto.MileageRefundDto;
import java.util.List;

public interface MileageQueryRepository {

    void updateMileages(List<MileageDto> mileages);

    void updateRefundMileages(List<MileageRefundDto> refundMileages);
}
