package com.connectly.partnerAdmin.module.order.repository;

import com.connectly.partnerAdmin.module.order.dto.OrderProduct;
import com.connectly.partnerAdmin.module.order.dto.OrderSnapShotProductGroupQueryDto;
import com.connectly.partnerAdmin.module.order.dto.OrderSnapShotProductQueryDto;

import java.util.List;

public interface OrderSnapShotFetchRepository {
    List<OrderProduct> fetchOrderProductOption(Long paymentId, List<Long> orderIds);

    List<OrderSnapShotProductGroupQueryDto> fetchOrderSnapShotProductGroupQueryDto(List<Long> productIds);
    List<OrderSnapShotProductQueryDto> fetchOrderSnapShotProductQueryDto(List<Long> productIds);

}
