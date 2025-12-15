package com.setof.connectly.module.order.mapper;

import com.setof.connectly.module.order.dto.snapshot.OrderProductSnapShotQueryDto;
import com.setof.connectly.module.order.dto.snapshot.SnapShotQueryDto;
import java.util.List;

public interface OrderSnapShotMapper {
    SnapShotQueryDto toSnapShots(List<OrderProductSnapShotQueryDto> orderProducts);
}
