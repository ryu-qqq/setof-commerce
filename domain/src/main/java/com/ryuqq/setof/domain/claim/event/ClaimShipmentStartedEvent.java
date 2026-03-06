package com.ryuqq.setof.domain.claim.event;

import com.ryuqq.setof.domain.claim.id.ClaimShipmentId;
import com.ryuqq.setof.domain.common.event.DomainEvent;
import java.time.Instant;

/**
 * 클레임 수거 시작 이벤트.
 *
 * <p>클레임 배송이 수거 진행 상태(IN_TRANSIT)로 전이되었을 때 발행됩니다.
 *
 * @param claimShipmentId 클레임 배송 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record ClaimShipmentStartedEvent(ClaimShipmentId claimShipmentId, Instant occurredAt)
        implements DomainEvent {

    public static ClaimShipmentStartedEvent of(
            ClaimShipmentId claimShipmentId, Instant occurredAt) {
        return new ClaimShipmentStartedEvent(claimShipmentId, occurredAt);
    }
}
