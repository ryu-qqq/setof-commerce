package com.ryuqq.setof.domain.order.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import com.ryuqq.setof.domain.order.id.OrderItemId;
import java.time.Instant;

/**
 * 주문 아이템 배송 완료 이벤트.
 *
 * <p>주문 아이템의 배송이 완료되었을 때 발행됩니다.
 *
 * @param orderItemId 주문 아이템 ID
 * @param orderId 주문 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record OrderItemDeliveredEvent(
        OrderItemId orderItemId, LegacyOrderId orderId, Instant occurredAt) implements DomainEvent {

    public static OrderItemDeliveredEvent of(
            OrderItemId orderItemId, LegacyOrderId orderId, Instant now) {
        return new OrderItemDeliveredEvent(orderItemId, orderId, now);
    }

    public Long orderItemIdValue() {
        return orderItemId.value();
    }

    public Long orderIdValue() {
        return orderId.value();
    }
}
