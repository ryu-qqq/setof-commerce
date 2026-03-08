package com.ryuqq.setof.domain.exchange.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.exchange.id.ExchangeId;
import com.ryuqq.setof.domain.exchange.vo.ExchangeTarget;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.time.Instant;

/**
 * 교환 요청 이벤트.
 *
 * <p>교환이 요청되었을 때 발행됩니다.
 *
 * @param exchangeId 교환 ID
 * @param orderId 주문 ID
 * @param sellerId 판매자 ID
 * @param exchangeTarget 교환 대상 상품 정보
 * @param occurredAt 이벤트 발생 시각
 */
public record ExchangeRequestedEvent(
        ExchangeId exchangeId,
        LegacyOrderId orderId,
        SellerId sellerId,
        ExchangeTarget exchangeTarget,
        Instant occurredAt)
        implements DomainEvent {

    public static ExchangeRequestedEvent of(
            ExchangeId exchangeId,
            LegacyOrderId orderId,
            SellerId sellerId,
            ExchangeTarget exchangeTarget,
            Instant now) {
        return new ExchangeRequestedEvent(exchangeId, orderId, sellerId, exchangeTarget, now);
    }

    public Long exchangeIdValue() {
        return exchangeId.value();
    }

    public Long orderIdValue() {
        return orderId.value();
    }

    public Long sellerIdValue() {
        return sellerId.value();
    }
}
