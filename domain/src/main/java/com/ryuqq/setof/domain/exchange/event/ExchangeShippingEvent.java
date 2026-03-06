package com.ryuqq.setof.domain.exchange.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.exchange.id.ExchangeId;
import com.ryuqq.setof.domain.order.id.OrderId;
import java.time.Instant;

/**
 * 교환 상품 배송 시작 이벤트.
 *
 * <p>교환 상품 배송이 시작되었을 때 발행됩니다.
 *
 * @param exchangeId 교환 ID
 * @param orderId 주문 ID
 * @param linkedOrderId 교환 상품 신규 주문 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record ExchangeShippingEvent(
        ExchangeId exchangeId, OrderId orderId, OrderId linkedOrderId, Instant occurredAt)
        implements DomainEvent {

    public static ExchangeShippingEvent of(
            ExchangeId exchangeId, OrderId orderId, OrderId linkedOrderId, Instant now) {
        return new ExchangeShippingEvent(exchangeId, orderId, linkedOrderId, now);
    }

    public Long exchangeIdValue() {
        return exchangeId.value();
    }

    public Long orderIdValue() {
        return orderId.value();
    }

    public Long linkedOrderIdValue() {
        return linkedOrderId.value();
    }
}
