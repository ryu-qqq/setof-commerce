package com.ryuqq.setof.domain.exchange.event;

import com.ryuqq.setof.domain.claim.id.ClaimShipmentId;
import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.exchange.id.ExchangeId;
import java.time.Instant;

/**
 * 교환 수거 시작 이벤트.
 *
 * <p>교환 상품 수거가 시작되었을 때 발행됩니다.
 *
 * @param exchangeId 교환 ID
 * @param claimShipmentId 클레임 배송 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record ExchangeCollectingStartedEvent(
        ExchangeId exchangeId, ClaimShipmentId claimShipmentId, Instant occurredAt)
        implements DomainEvent {

    public static ExchangeCollectingStartedEvent of(
            ExchangeId exchangeId, ClaimShipmentId claimShipmentId, Instant now) {
        return new ExchangeCollectingStartedEvent(exchangeId, claimShipmentId, now);
    }

    public Long exchangeIdValue() {
        return exchangeId.value();
    }

    public Long claimShipmentIdValue() {
        return claimShipmentId.value();
    }
}
