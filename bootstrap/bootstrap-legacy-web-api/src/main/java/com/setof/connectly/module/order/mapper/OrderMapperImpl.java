package com.setof.connectly.module.order.mapper;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.order.dto.OrderCountDto;
import com.setof.connectly.module.order.dto.fetch.OrderResponse;
import com.setof.connectly.module.order.dto.fetch.UpdateOrderResponse;
import com.setof.connectly.module.order.dto.order.OrderSheet;
import com.setof.connectly.module.order.dto.slice.OrderSlice;
import com.setof.connectly.module.order.entity.order.Order;
import com.setof.connectly.module.order.entity.order.OrderHistory;
import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.utils.SecurityUtils;
import com.setof.connectly.module.utils.SliceUtils;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public Order toEntity(OrderSheet createOrder) {
        return Order.builder()
                .paymentId(createOrder.getPaymentId())
                .productId(createOrder.getProductId())
                .sellerId(createOrder.getSellerId())
                .orderAmount(createOrder.getOrderAmount())
                .quantity(createOrder.getQuantity())
                .userId(SecurityUtils.currentUserId())
                .orderStatus(OrderStatus.ORDER_PROCESSING)
                .reviewYn(Yn.N)
                .build();
    }

    @Override
    public OrderHistory toHistoryEntity(Order order) {
        return OrderHistory.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .changeReason("")
                .changeDetailReason("")
                .build();
    }

    @Override
    public OrderSlice<OrderResponse> toSlice(
            List<OrderResponse> orders, Pageable pageable, List<OrderCountDto> orderCounts) {
        Slice<OrderResponse> orderResponses = SliceUtils.toSlice(orders, pageable);
        Long lastDomainId = null;

        if (!orderResponses.isEmpty()) {
            List<OrderResponse> content = orderResponses.getContent();
            OrderResponse lastItem = content.get(content.size() - 1);
            lastDomainId = lastItem.getOrderProduct().getOrderId();
        }

        return OrderSlice.<OrderResponse>builder()
                .content(orderResponses.getContent())
                .last(orderResponses.isLast())
                .first(orderResponses.isFirst())
                .number(orderResponses.getNumber())
                .sort(orderResponses.getSort())
                .size(orderResponses.getSize())
                .numberOfElements(orderResponses.getNumberOfElements())
                .empty(orderResponses.isEmpty())
                .lastDomainId(lastDomainId)
                .originSlice(orderResponses)
                .orderCounts(orderCounts)
                .build();
    }

    @Override
    public List<OrderCountDto> setOrderCount(Map<OrderStatus, Long> orderStatusLongMap) {

        return orderStatusLongMap.entrySet().stream()
                .map(
                        k -> {
                            return new OrderCountDto(k.getKey(), k.getValue());
                        })
                .collect(Collectors.toList());
    }

    @Override
    public UpdateOrderResponse toUpdateOrderResponse(
            OrderStatus pastOrderStatus,
            Order order,
            String changeReason,
            String changeDetailReason) {
        return UpdateOrderResponse.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .asIsOrderStatus(pastOrderStatus)
                .toBeOrderStatus(order.getOrderStatus())
                .changeReason(changeReason)
                .changeDetailReason(changeDetailReason)
                .build();
    }
}
