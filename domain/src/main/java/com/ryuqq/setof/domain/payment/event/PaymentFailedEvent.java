package com.ryuqq.setof.domain.payment.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.order.id.OrderId;
import com.ryuqq.setof.domain.payment.id.PaymentId;
import java.time.Instant;

/**
 * 결제 실패 이벤트.
 *
 * <p>결제가 실패했을 때 발행됩니다.
 *
 * @param paymentId 결제 ID
 * @param orderId 주문 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record PaymentFailedEvent(PaymentId paymentId, OrderId orderId, Instant occurredAt)
        implements DomainEvent {

    public static PaymentFailedEvent of(PaymentId paymentId, OrderId orderId, Instant now) {
        return new PaymentFailedEvent(paymentId, orderId, now);
    }

    public Long paymentIdValue() {
        return paymentId.value();
    }

    public Long orderIdValue() {
        return orderId.value();
    }
}
