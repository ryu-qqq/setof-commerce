package com.ryuqq.setof.domain.refund.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.refund.id.RefundId;
import java.time.Instant;

/**
 * 반품 보류 이벤트.
 *
 * <p>반품이 보류 처리되었을 때 발행됩니다.
 *
 * @param refundId 반품 ID
 * @param holdReason 보류 사유
 * @param occurredAt 이벤트 발생 시각
 */
public record RefundHeldEvent(RefundId refundId, String holdReason, Instant occurredAt)
        implements DomainEvent {

    public static RefundHeldEvent of(RefundId refundId, String holdReason, Instant now) {
        return new RefundHeldEvent(refundId, holdReason, now);
    }

    public Long refundIdValue() {
        return refundId.value();
    }
}
