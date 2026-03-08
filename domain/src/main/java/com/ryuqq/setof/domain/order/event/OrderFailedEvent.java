package com.ryuqq.setof.domain.order.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import java.time.Instant;

/**
 * 주문 실패 이벤트.
 *
 * <p>결제 실패로 주문이 FAILED 상태가 되었을 때 발행됩니다. 재고 선점 해제를 트리거합니다.
 *
 * @param orderId 주문 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record OrderFailedEvent(LegacyOrderId orderId, Instant occurredAt) implements DomainEvent {

    public static OrderFailedEvent of(LegacyOrderId orderId, Instant now) {
        return new OrderFailedEvent(orderId, now);
    }

    public Long orderIdValue() {
        return orderId.value();
    }
}
