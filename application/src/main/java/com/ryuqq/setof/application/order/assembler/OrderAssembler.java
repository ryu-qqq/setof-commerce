package com.ryuqq.setof.application.order.assembler;

import com.ryuqq.setof.application.order.dto.response.OrderItemResponse;
import com.ryuqq.setof.application.order.dto.response.OrderResponse;
import com.ryuqq.setof.domain.order.aggregate.Order;
import com.ryuqq.setof.domain.order.aggregate.OrderItem;
import com.ryuqq.setof.domain.order.vo.ShippingInfo;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 주문 Assembler
 *
 * <p>Domain 객체를 Response DTO로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class OrderAssembler {

    /**
     * 주문 응답 변환
     *
     * @param order 주문 도메인 객체
     * @return OrderResponse DTO
     */
    public OrderResponse toResponse(Order order) {
        ShippingInfo shippingInfo = order.shippingInfo();
        List<OrderItemResponse> itemResponses = toItemResponses(order.items());

        return new OrderResponse(
                order.id().value().toString(),
                order.orderNumber().value(),
                order.checkoutId().value().toString(),
                order.paymentId().value().toString(),
                order.sellerId(),
                order.memberId(),
                order.status().name(),
                itemResponses,
                shippingInfo != null ? shippingInfo.receiverName() : null,
                shippingInfo != null ? shippingInfo.receiverPhone() : null,
                shippingInfo != null ? shippingInfo.address() : null,
                shippingInfo != null ? shippingInfo.addressDetail() : null,
                shippingInfo != null ? shippingInfo.zipCode() : null,
                shippingInfo != null ? shippingInfo.memo() : null,
                order.totalItemAmount().value(),
                order.shippingFee().value(),
                order.totalAmount().value(),
                order.orderedAt(),
                order.confirmedAt(),
                order.shippedAt(),
                order.deliveredAt(),
                order.completedAt(),
                order.cancelledAt());
    }

    private List<OrderItemResponse> toItemResponses(List<OrderItem> items) {
        return items.stream().map(this::toItemResponse).toList();
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.id().value().toString(),
                item.productId(),
                item.productStockId(),
                item.orderedQuantity(),
                item.cancelledQuantity(),
                item.refundedQuantity(),
                item.effectiveQuantity(),
                item.unitPrice().value(),
                item.totalPrice().value(),
                item.status().name(),
                item.snapshot().productName(),
                item.snapshot().productImage(),
                item.snapshot().optionName(),
                item.snapshot().brandName(),
                item.snapshot().sellerName());
    }
}
