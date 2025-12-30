package com.connectly.partnerAdmin.module.order.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.common.filter.BaseDateTimeRangeFilter;
import com.connectly.partnerAdmin.module.common.filter.BaseRoleFilter;
import com.connectly.partnerAdmin.module.notification.mapper.order.ProductOrderSheet;
import com.connectly.partnerAdmin.module.order.dto.OrderDashboardResponse;
import com.connectly.partnerAdmin.module.order.dto.OrderDateDashboardResponse;
import com.connectly.partnerAdmin.module.order.dto.OrderResponse;
import com.connectly.partnerAdmin.module.order.dto.SettlementResponse;
import com.connectly.partnerAdmin.module.order.dto.response.OrderListResponse;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.filter.OrderFilter;

public interface OrderFetchService {
    Order fetchOrderEntity(long orderId);
    List<Order> fetchOrderEntities(List<Long> orderIds);
    OrderResponse fetchOrder(long orderId);
    CustomPageable<OrderListResponse> fetchOrders(OrderFilter filter, Pageable pageable);
    CustomPageable<SettlementResponse> fetchSettlements(BaseDateTimeRangeFilter filter, Pageable pageable);
    List<ProductOrderSheet> fetchProductOrderSheets(List<Long> orderIds);
    OrderDashboardResponse fetchOrderTodayDashboard(BaseRoleFilter filter);
    OrderDateDashboardResponse fetchOrderDateDashboard(BaseDateTimeRangeFilter filter);
}
