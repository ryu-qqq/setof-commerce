package com.ryuqq.setof.domain.refund.event;

import com.ryuqq.setof.domain.claim.id.ClaimShipmentId;
import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.refund.id.RefundId;
import java.time.Instant;

/**
 * 반품 수거 시작 이벤트.
 *
 * <p>반품 상품의 수거가 시작되었을 때 발행됩니다.
 *
 * @param refundId 반품 ID
 * @param claimShipmentId 클레임 배송 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record RefundCollectingStartedEvent(
        RefundId refundId, ClaimShipmentId claimShipmentId, Instant occurredAt)
        implements DomainEvent {

    public static RefundCollectingStartedEvent of(
            RefundId refundId, ClaimShipmentId claimShipmentId, Instant now) {
        return new RefundCollectingStartedEvent(refundId, claimShipmentId, now);
    }

    public Long refundIdValue() {
        return refundId.value();
    }

    public Long claimShipmentIdValue() {
        return claimShipmentId.value();
    }
}
