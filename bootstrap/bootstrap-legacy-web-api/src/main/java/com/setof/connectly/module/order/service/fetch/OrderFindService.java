package com.setof.connectly.module.order.service.fetch;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.display.entity.embedded.DisplayPeriod;
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
import com.setof.connectly.module.review.dto.ReviewOrderProductDto;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface OrderFindService {

    Order fetchOrderEntity(long orderId);

    List<Order> fetchOrderEntities(long paymentId);

    Set<OrderProductDto> fetchOrderProducts(List<Long> orderIds, List<OrderStatus> orderStatuses);

    OrderSlice<OrderResponse> fetchOrders(OrderFilter filterDto, Pageable pageable);

    Map<OrderStatus, Long> fetchCountOrdersByStatusInMyPage(
            long userId, List<OrderStatus> orderStatuses);

    List<OrderCountDto> fetchOrderCounts(List<OrderStatus> orderStatuses);

    CustomSlice<ReviewOrderProductDto> fetchAvailableReviewOrders(
            Long lastDomainId, Pageable pageable);

    List<OrderRejectReason> fetchRejectedOrder(List<Long> orderIds);

    Optional<OrderHistoryResponse> fetchOrderHistory(long orderId, OrderStatus orderStatus);

    List<OrderHistoryResponse> fetchOrderHistories(long orderId);

    Optional<PendingMileageQueryDto> fetchPendingMileage(long paymentId);

    List<ProductOrderSheet> fetchProductOrderSheets(List<Long> orderIds);

    Set<OrderProductDto> fetchProductsOrderedWithinPeriod(
            Set<Long> productIds, DisplayPeriod displayPeriod);

    boolean isRaffleOrderProduct(long orderId);
}
