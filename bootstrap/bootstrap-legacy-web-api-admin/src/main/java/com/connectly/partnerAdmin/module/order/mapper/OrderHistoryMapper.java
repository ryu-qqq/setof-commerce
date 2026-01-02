package com.connectly.partnerAdmin.module.order.mapper;

import com.connectly.partnerAdmin.module.order.dto.OrderHistoryResponse;
import com.connectly.partnerAdmin.module.order.entity.Order;

import java.util.List;

public interface OrderHistoryMapper {

    List<OrderHistoryResponse> toOrderHistories(Order order);
}
