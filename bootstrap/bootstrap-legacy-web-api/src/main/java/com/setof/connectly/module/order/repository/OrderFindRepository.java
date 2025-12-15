package com.setof.connectly.module.order.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.setof.connectly.module.display.entity.embedded.DisplayPeriod;
import com.setof.connectly.module.mileage.dto.query.PendingMileageQueryDto;
import com.setof.connectly.module.notification.dto.order.ProductOrderSheet;
import com.setof.connectly.module.order.dto.OrderProductDto;
import com.setof.connectly.module.order.dto.OrderRejectReason;
import com.setof.connectly.module.order.dto.fetch.OrderHistoryResponse;
import com.setof.connectly.module.order.dto.fetch.OrderResponse;
import com.setof.connectly.module.order.dto.filter.OrderFilter;
import com.setof.connectly.module.order.entity.order.Order;
import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.review.dto.ReviewOrderProductDto;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface OrderFindRepository {

    Optional<Order> fetchOrderEntity(long orderId);

    List<Order> fetchOrderEntities(long paymentId);

    List<OrderProductDto> fetchOrderProducts(List<Long> orderIds, List<OrderStatus> orderStatuses);

    Map<OrderStatus, Long> countOrdersByStatusInMyPage(
            long userId, List<OrderStatus> orderStatuses);

    List<OrderResponse> fetchOrders(long userId, OrderFilter filterDto, Pageable pageable);

    List<ReviewOrderProductDto> fetchAvailableReviews(
            long userId, Long lastDomainId, Pageable pageable);

    JPAQuery<Long> fetchAvailableReviewsCountQuery(long userId);

    Optional<OrderHistoryResponse> fetchOrderHistory(long orderId, OrderStatus orderStatus);

    List<OrderHistoryResponse> fetchOrderHistories(long orderId);

    List<OrderRejectReason> fetchRejectedOrder(List<Long> orderIds);

    Optional<PendingMileageQueryDto> fetchPendingMileage(long paymentId);

    List<ProductOrderSheet> fetchProductOrderSheets(List<Long> orderIds);

    List<OrderProductDto> fetchProductsOrderedWithinPeriod(
            long userId, Set<Long> productIds, DisplayPeriod displayPeriod);

    boolean isRaffleOrderProduct(long orderId);
}
