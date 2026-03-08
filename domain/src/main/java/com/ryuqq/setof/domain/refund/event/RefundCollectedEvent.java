package com.ryuqq.setof.domain.refund.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import com.ryuqq.setof.domain.refund.id.RefundId;
import java.time.Instant;

/**
 * 반품 수거 완료 이벤트.
 *
 * <p>반품 상품의 수거가 완료되었을 때 발행됩니다.
 *
 * @param refundId 반품 ID
 * @param orderId 주문 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record RefundCollectedEvent(RefundId refundId, LegacyOrderId orderId, Instant occurredAt)
        implements DomainEvent {

    public static RefundCollectedEvent of(RefundId refundId, LegacyOrderId orderId, Instant now) {
        return new RefundCollectedEvent(refundId, orderId, now);
    }

    public Long refundIdValue() {
        return refundId.value();
    }

    public Long orderIdValue() {
        return orderId.value();
    }
}
