package com.ryuqq.setof.domain.cancel.event;

import com.ryuqq.setof.domain.cancel.id.CancelId;
import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import java.time.Instant;

/**
 * 취소 승인 이벤트.
 *
 * <p>취소가 승인되었을 때 발행됩니다.
 *
 * @param cancelId 취소 ID
 * @param orderId 주문 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record CancelApprovedEvent(CancelId cancelId, LegacyOrderId orderId, Instant occurredAt)
        implements DomainEvent {

    public static CancelApprovedEvent of(
            CancelId cancelId, LegacyOrderId orderId, Instant occurredAt) {
        return new CancelApprovedEvent(cancelId, orderId, occurredAt);
    }
}
