package com.connectly.partnerAdmin.module.order.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.connectly.partnerAdmin.module.common.filter.BaseDateTimeRangeFilter;
import com.connectly.partnerAdmin.module.common.filter.BaseRoleFilter;
import com.connectly.partnerAdmin.module.notification.mapper.order.ProductOrderSheet;
import com.connectly.partnerAdmin.module.order.dto.OrderDashboardRanking;
import com.connectly.partnerAdmin.module.order.dto.OrderHistoryResponse;
import com.connectly.partnerAdmin.module.order.dto.OrderResponse;
import com.connectly.partnerAdmin.module.order.dto.OrderStatistics;
import com.connectly.partnerAdmin.module.order.dto.OrderTodayCountResponse;
import com.connectly.partnerAdmin.module.order.dto.SettlementResponse;
import com.connectly.partnerAdmin.module.order.dto.response.OrderListResponse;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.filter.OrderFilter;
import com.querydsl.jpa.impl.JPAQuery;

public interface OrderFetchRepository {

    Optional<Order> fetchOrderEntity(long orderId, Optional<Long> sellerIdOpt);

    List<Order> fetchOrderEntities(List<Long> orderIds, Optional<Long> sellerIdOpt);
    List<OrderHistoryResponse> fetchOrderHistoryEntities(List<Long> orderIds);
    Optional<OrderResponse> fetchOrder(long orderId, Optional<Long> sellerIdOpt);

    List<OrderListResponse> fetchOrders(OrderFilter filter, Pageable pageable);
    List<SettlementResponse> fetchSettlements(BaseDateTimeRangeFilter filter, Pageable pageable);
    JPAQuery<Long> fetchSettlementCount(BaseDateTimeRangeFilter filter);
    JPAQuery<Long> fetchOrderCountQuery(OrderFilter filter);

    List<ProductOrderSheet> fetchProductOrderSheets(List<Long> orderIds);

    List<Long> fetchOrderIds(Long lastDomainId);

    List<OrderStatistics> fetchOrderMonthStatistics(BaseRoleFilter filter);
    Optional<OrderStatistics> fetchOrderDateStatistics(BaseDateTimeRangeFilter filter);
    Optional<OrderTodayCountResponse> fetchOrderTodayCountQuery(BaseRoleFilter filter);
    List<OrderDashboardRanking> fetchOrderDashboardRanking(BaseDateTimeRangeFilter filter);
    List<OrderStatistics> fetchOrderExternalStatistics(BaseDateTimeRangeFilter filter);

}
