package com.ryuqq.setof.domain.cancel.event;

import com.ryuqq.setof.domain.cancel.id.CancelId;
import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.order.id.OrderId;
import java.time.Instant;

/**
 * 취소 거부 이벤트.
 *
 * <p>취소가 거부되었을 때 발행됩니다.
 *
 * @param cancelId 취소 ID
 * @param orderId 주문 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record CancelRejectedEvent(CancelId cancelId, OrderId orderId, Instant occurredAt)
        implements DomainEvent {

    public static CancelRejectedEvent of(CancelId cancelId, OrderId orderId, Instant occurredAt) {
        return new CancelRejectedEvent(cancelId, orderId, occurredAt);
    }
}
