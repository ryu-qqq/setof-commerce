package com.ryuqq.setof.domain.refund.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import com.ryuqq.setof.domain.refund.id.RefundId;
import java.time.Instant;

/**
 * 반품 거부 이벤트.
 *
 * <p>반품 요청이 거부되었을 때 발행됩니다.
 *
 * @param refundId 반품 ID
 * @param orderId 주문 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record RefundRejectedEvent(RefundId refundId, LegacyOrderId orderId, Instant occurredAt)
        implements DomainEvent {

    public static RefundRejectedEvent of(RefundId refundId, LegacyOrderId orderId, Instant now) {
        return new RefundRejectedEvent(refundId, orderId, now);
    }

    public Long refundIdValue() {
        return refundId.value();
    }

    public Long orderIdValue() {
        return orderId.value();
    }
}
