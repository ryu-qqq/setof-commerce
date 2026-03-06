package com.ryuqq.setof.domain.cancel.event;

import com.ryuqq.setof.domain.cancel.id.CancelId;
import com.ryuqq.setof.domain.cancel.vo.CancelType;
import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.order.id.OrderId;
import java.time.Instant;

/**
 * 취소 요청 이벤트.
 *
 * <p>취소가 요청되었을 때 발행됩니다.
 *
 * @param cancelId 취소 ID
 * @param orderId 주문 ID
 * @param cancelType 취소 유형
 * @param occurredAt 이벤트 발생 시각
 */
public record CancelRequestedEvent(
        CancelId cancelId, OrderId orderId, CancelType cancelType, Instant occurredAt)
        implements DomainEvent {

    public static CancelRequestedEvent of(
            CancelId cancelId, OrderId orderId, CancelType cancelType, Instant occurredAt) {
        return new CancelRequestedEvent(cancelId, orderId, cancelType, occurredAt);
    }
}
