package com.connectly.partnerAdmin.module.order.service;


import com.connectly.partnerAdmin.module.order.dto.OrderProduct;
import com.connectly.partnerAdmin.module.order.dto.OrderSnapShotQueryDto;

import java.util.List;

public interface OrderSnapShotFetchService {
    List<OrderProduct> fetchOrderSnapShotProducts(Long paymentId, List<Long> orderIds);
    List<OrderSnapShotQueryDto> fetchProductQueryForSnapShot(List<Long> productIds);

}
