package com.setof.connectly.module.order.service.fetch;

import com.querydsl.jpa.impl.JPAQuery;
import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.display.entity.embedded.DisplayPeriod;
import com.setof.connectly.module.exception.order.OrderNotFoundException;
import com.setof.connectly.module.mileage.dto.query.PendingMileageQueryDto;
import com.setof.connectly.module.notification.dto.order.ProductOrderSheet;
import com.setof.connectly.module.order.dto.OrderCountDto;
import com.setof.connectly.module.order.dto.OrderProductDto;
import com.setof.connectly.module.order.dto.OrderRejectReason;
import com.setof.connectly.module.order.dto.fetch.OrderHistoryResponse;
import com.setof.connectly.module.order.dto.fetch.OrderResponse;
import com.setof.connectly.module.order.dto.filter.OrderFilter;
import com.setof.connectly.module.order.dto.slice.OrderSlice;
import com.setof.connectly.module.order.entity.order.Order;
import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.order.mapper.OrderMapper;
import com.setof.connectly.module.order.mapper.OrderSliceMapper;
import com.setof.connectly.module.order.repository.OrderFindRepository;
import com.setof.connectly.module.review.dto.ReviewOrderProductDto;
import com.setof.connectly.module.utils.SecurityUtils;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class OrderFindServiceImpl implements OrderFindService {

    private final OrderFindRepository orderFindRepository;
    private final OrderMapper orderMapper;

    private final OrderSliceMapper orderSliceMapper;

    @Override
    public Order fetchOrderEntity(long orderId) {
        return orderFindRepository
                .fetchOrderEntity(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Override
    public List<Order> fetchOrderEntities(long paymentId) {
        return orderFindRepository.fetchOrderEntities(paymentId);
    }

    @Override
    public Set<OrderProductDto> fetchOrderProducts(
            List<Long> orderIds, List<OrderStatus> orderStatuses) {
        List<OrderProductDto> orderProducts =
                orderFindRepository.fetchOrderProducts(orderIds, orderStatuses);
        return new HashSet<>(orderProducts);
    }

    @Override
    public List<OrderRejectReason> fetchRejectedOrder(List<Long> orderIds) {
        return orderFindRepository.fetchRejectedOrder(orderIds);
    }

    @Override
    public OrderSlice<OrderResponse> fetchOrders(OrderFilter filterDto, Pageable pageable) {
        List<OrderResponse> orderResponses =
                orderFindRepository.fetchOrders(SecurityUtils.currentUserId(), filterDto, pageable);
        List<OrderCountDto> orderCounts = fetchOrderCounts(filterDto.getOrderStatusList());
        return orderMapper.toSlice(orderResponses, pageable, orderCounts);
    }

    @Override
    public Map<OrderStatus, Long> fetchCountOrdersByStatusInMyPage(
            long userId, List<OrderStatus> orderStatuses) {
        return orderFindRepository.countOrdersByStatusInMyPage(userId, orderStatuses);
    }

    @Override
    public List<OrderCountDto> fetchOrderCounts(List<OrderStatus> orderStatuses) {
        long userId = SecurityUtils.currentUserId();
        Map<OrderStatus, Long> orderStatusLongMap =
                fetchCountOrdersByStatusInMyPage(userId, orderStatuses);
        return orderMapper.setOrderCount(orderStatusLongMap);
    }

    @Override
    public CustomSlice<ReviewOrderProductDto> fetchAvailableReviewOrders(
            Long lastDomainId, Pageable pageable) {
        long userId = SecurityUtils.currentUserId();
        List<ReviewOrderProductDto> reviewOrderProducts =
                orderFindRepository.fetchAvailableReviews(userId, lastDomainId, pageable);
        JPAQuery<Long> total = orderFindRepository.fetchAvailableReviewsCountQuery(userId);
        return orderSliceMapper.toSlice(reviewOrderProducts, pageable, total.fetchCount());
    }

    @Override
    public List<OrderHistoryResponse> fetchOrderHistories(long orderId) {
        return orderFindRepository.fetchOrderHistories(orderId);
    }

    @Override
    public Optional<OrderHistoryResponse> fetchOrderHistory(long orderId, OrderStatus orderStatus) {
        return orderFindRepository.fetchOrderHistory(orderId, orderStatus);
    }

    @Override
    public Optional<PendingMileageQueryDto> fetchPendingMileage(long paymentId) {
        return orderFindRepository.fetchPendingMileage(paymentId);
    }

    @Override
    public List<ProductOrderSheet> fetchProductOrderSheets(List<Long> orderIds) {
        return orderFindRepository.fetchProductOrderSheets(orderIds);
    }

    @Override
    public Set<OrderProductDto> fetchProductsOrderedWithinPeriod(
            Set<Long> productIds, DisplayPeriod displayPeriod) {
        long userId = SecurityUtils.currentUserId();
        List<OrderProductDto> orderProducts =
                orderFindRepository.fetchProductsOrderedWithinPeriod(
                        userId, productIds, displayPeriod);
        return new HashSet<>(orderProducts);
    }

    public boolean isRaffleOrderProduct(long orderId) {
        return orderFindRepository.isRaffleOrderProduct(orderId);
    }
}
