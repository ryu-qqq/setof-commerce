package com.ryuqq.setof.domain.exchange.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.exchange.id.ExchangeId;
import com.ryuqq.setof.domain.order.id.OrderId;
import java.time.Instant;

/**
 * 교환 수거 완료 이벤트.
 *
 * <p>교환 상품 수거가 완료되었을 때 발행됩니다.
 *
 * @param exchangeId 교환 ID
 * @param orderId 주문 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record ExchangeCollectedEvent(ExchangeId exchangeId, OrderId orderId, Instant occurredAt)
        implements DomainEvent {

    public static ExchangeCollectedEvent of(ExchangeId exchangeId, OrderId orderId, Instant now) {
        return new ExchangeCollectedEvent(exchangeId, orderId, now);
    }

    public Long exchangeIdValue() {
        return exchangeId.value();
    }

    public Long orderIdValue() {
        return orderId.value();
    }
}
