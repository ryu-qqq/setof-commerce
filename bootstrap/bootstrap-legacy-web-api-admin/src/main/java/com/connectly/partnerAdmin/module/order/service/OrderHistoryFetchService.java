package com.connectly.partnerAdmin.module.order.service;

import java.util.List;

import com.connectly.partnerAdmin.module.order.dto.OrderHistoryResponse;

public interface OrderHistoryFetchService {
    List<OrderHistoryResponse> fetchOrderHistories(long orderId);
    List<OrderHistoryResponse> fetchOrderHistories(List<Long> orderIds);

}
