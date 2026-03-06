package com.ryuqq.setof.domain.order.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.order.id.OrderId;
import com.ryuqq.setof.domain.order.id.OrderItemId;
import java.time.Instant;

/**
 * 주문 아이템 확인 이벤트.
 *
 * <p>주문 아이템이 확인되었을 때 발행됩니다.
 *
 * @param orderItemId 주문 아이템 ID
 * @param orderId 주문 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record OrderItemConfirmedEvent(OrderItemId orderItemId, OrderId orderId, Instant occurredAt)
        implements DomainEvent {

    public static OrderItemConfirmedEvent of(
            OrderItemId orderItemId, OrderId orderId, Instant now) {
        return new OrderItemConfirmedEvent(orderItemId, orderId, now);
    }

    public Long orderItemIdValue() {
        return orderItemId.value();
    }

    public Long orderIdValue() {
        return orderId.value();
    }
}
