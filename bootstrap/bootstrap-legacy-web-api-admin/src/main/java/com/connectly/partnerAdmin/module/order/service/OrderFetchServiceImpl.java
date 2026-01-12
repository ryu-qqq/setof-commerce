package com.connectly.partnerAdmin.module.order.service;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.common.filter.BaseDateTimeRangeFilter;
import com.connectly.partnerAdmin.module.common.filter.BaseRoleFilter;
import com.connectly.partnerAdmin.module.notification.mapper.order.ProductOrderSheet;
import com.connectly.partnerAdmin.module.order.dto.OrderDashboardRanking;
import com.connectly.partnerAdmin.module.order.dto.OrderDashboardResponse;
import com.connectly.partnerAdmin.module.order.dto.OrderDateDashboardResponse;
import com.connectly.partnerAdmin.module.order.dto.OrderHistoryResponse;
import com.connectly.partnerAdmin.module.order.dto.OrderProduct;
import com.connectly.partnerAdmin.module.order.dto.OrderResponse;
import com.connectly.partnerAdmin.module.order.dto.OrderStatistics;
import com.connectly.partnerAdmin.module.order.dto.OrderTodayCountResponse;
import com.connectly.partnerAdmin.module.order.dto.SettlementResponse;
import com.connectly.partnerAdmin.module.order.dto.TodayDashboard;
import com.connectly.partnerAdmin.module.order.dto.response.OrderListResponse;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.exception.OrderNotFoundException;
import com.connectly.partnerAdmin.module.order.filter.OrderFilter;
import com.connectly.partnerAdmin.module.order.mapper.OrderPageableMapper;
import com.connectly.partnerAdmin.module.order.repository.OrderFetchRepository;
import com.connectly.partnerAdmin.module.qna.dto.fetch.QnaCountResponse;
import com.connectly.partnerAdmin.module.qna.repository.QnaFetchRepository;
import com.connectly.partnerAdmin.module.utils.SecurityUtils;
import com.querydsl.jpa.impl.JPAQuery;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderFetchServiceImpl implements OrderFetchService {

    private final OrderFetchRepository orderFetchRepository;
    private final OrderSnapShotFetchService orderSnapShotFetchService;
    private final OrderHistoryFetchService orderHistoryFetchService;
    private final OrderPageableMapper orderPageableMapper;
    private final QnaFetchRepository qnaFetchRepository;

    @Override
    public Order fetchOrderEntity(long orderId) {
        return orderFetchRepository.fetchOrderEntity(orderId, SecurityUtils.currentSellerIdOpt())
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Override
    public List<Order> fetchOrderEntities(List<Long> orderIds) {
        return orderFetchRepository.fetchOrderEntities(orderIds, SecurityUtils.currentSellerIdOpt());
    }

    @Override
    public OrderResponse fetchOrder(long orderId) {
        OrderResponse orderResponse = orderFetchRepository.fetchOrder(orderId, SecurityUtils.currentSellerIdOpt())
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        List<OrderProduct> orderProducts = orderSnapShotFetchService.fetchOrderSnapShotProducts(null, Collections.singletonList(orderId));

        LocalDateTime paymentDate = orderResponse.getPayment().getPaymentDate();
        orderResponse.setOrderProduct(orderProducts.getFirst(), paymentDate);

        return orderResponse;
    }


    @Override
    public CustomPageable<OrderListResponse> fetchOrders(OrderFilter filter, Pageable pageable) {
        List<OrderListResponse> results = orderFetchRepository.fetchOrders(filter, pageable);

        // 조회 결과가 없으면 스냅샷/히스토리 조회 없이 조기 반환 (풀스캔 방지)
        if (results.isEmpty()) {
            long totalCount = fetchOrderCount(filter);
            return orderPageableMapper.toOrderResponse(results, pageable, totalCount);
        }

        List<Long> orderIds = results.stream()
                .map(OrderListResponse::getOrderId)
                .collect(Collectors.toList());

        List<OrderProduct> orderProducts = orderSnapShotFetchService.fetchOrderSnapShotProducts(null, orderIds);


        List<OrderHistoryResponse> orderHistoryResponses = orderHistoryFetchService.fetchOrderHistories(orderIds);

        Map<Long, List<OrderHistoryResponse>> orderHistoryMap = orderHistoryResponses.stream()
            .collect(Collectors.groupingBy(OrderHistoryResponse::getOrderId));


        Map<Long, OrderProduct> orderIdMap = orderProducts.stream()
                .collect(Collectors.toMap(OrderProduct::getOrderId,
                        Function.identity(), (v1, v2) -> v1));

        results.forEach(orderResponse -> {
            long orderId = orderResponse.getOrderId();
            LocalDateTime paymentDate = orderResponse.getPayment().getPaymentDate();
            OrderProduct orderProduct = orderIdMap.get(orderId);
            orderResponse.setOrderProduct(orderProduct, paymentDate);

            orderResponse.setOrderHistories(orderHistoryMap.getOrDefault(orderId, Collections.emptyList()));

        });

        long totalCount = fetchOrderCount(filter);

        return orderPageableMapper.toOrderResponse(results, pageable, totalCount);
    }

    @Override
    public CustomPageable<SettlementResponse> fetchSettlements(BaseDateTimeRangeFilter filter, Pageable pageable) {
        List<SettlementResponse> results = orderFetchRepository.fetchSettlements(filter, pageable);
        JPAQuery<Long> longJPAQuery = orderFetchRepository.fetchSettlementCount(filter);
        Long totalCount = longJPAQuery.fetchOne();
        return orderPageableMapper.toSettlementResponse(results, pageable, totalCount);
    }

    private long fetchOrderCount(OrderFilter filter) {
        JPAQuery<Long> longJPAQuery = orderFetchRepository.fetchOrderCountQuery(filter);
        Long totalCount = longJPAQuery.fetchOne();
        if (totalCount == null) {
            return 0L;
        }
        return totalCount;
    }

    @Override
    public List<ProductOrderSheet> fetchProductOrderSheets(List<Long> orderIds){
        return orderFetchRepository.fetchProductOrderSheets(orderIds);
    }

    @Override
    public OrderDashboardResponse fetchOrderTodayDashboard(BaseRoleFilter filter) {
        List<OrderStatistics> monthStatistics = orderFetchRepository.fetchOrderMonthStatistics(filter);
        Optional<QnaCountResponse> qnaCountResponse = qnaFetchRepository.fetchTodayQnaCountQuery(filter);
        Optional<OrderTodayCountResponse> response = orderFetchRepository.fetchOrderTodayCountQuery(filter);
        TodayDashboard todayDashBoard = new TodayDashboard(
                response.map(OrderTodayCountResponse::getOrderCount).orElse(0L),
                response.map(OrderTodayCountResponse::getClaimCount).orElse(0L),
                qnaCountResponse.map(QnaCountResponse::getOrderQnaCount).orElse(0L),
                qnaCountResponse.map(QnaCountResponse::getProductQnaCount).orElse(0L)
        );

        return new OrderDashboardResponse(todayDashBoard, monthStatistics);
    }

    @Override
    public OrderDateDashboardResponse fetchOrderDateDashboard(BaseDateTimeRangeFilter filter) {
        Optional<OrderStatistics> orderStatistics = orderFetchRepository.fetchOrderDateStatistics(filter);
        List<OrderDashboardRanking> dashboardRankings = orderFetchRepository.fetchOrderDashboardRanking(filter);
        List<OrderStatistics> orderExternalStatistics = orderFetchRepository.fetchOrderExternalStatistics(filter);

        return new OrderDateDashboardResponse(
                orderStatistics.orElseGet(() ->
                        OrderStatistics.builder()
                                .flag("Total")
                                .orderCount(0L)
                                .pureOrderCount(0L)
                                .orderAmount(BigDecimal.ZERO)
                                .pureOrderAmount(BigDecimal.ZERO)
                                .build()
                ),
                dashboardRankings,
                orderExternalStatistics
        );
    }


}

