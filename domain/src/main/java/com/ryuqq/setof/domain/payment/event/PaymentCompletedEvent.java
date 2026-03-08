package com.ryuqq.setof.domain.payment.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import com.ryuqq.setof.domain.payment.id.PaymentId;
import com.ryuqq.setof.domain.payment.vo.PaymentMethodType;
import java.time.Instant;

/**
 * 결제 완료 이벤트.
 *
 * <p>결제가 성공적으로 완료되었을 때 발행됩니다.
 *
 * @param paymentId 결제 ID
 * @param orderId 주문 ID
 * @param paymentAmount 결제 금액
 * @param methodType 결제 수단
 * @param occurredAt 이벤트 발생 시각
 */
public record PaymentCompletedEvent(
        PaymentId paymentId,
        LegacyOrderId orderId,
        Money paymentAmount,
        PaymentMethodType methodType,
        Instant occurredAt)
        implements DomainEvent {

    public static PaymentCompletedEvent of(
            PaymentId paymentId,
            LegacyOrderId orderId,
            Money paymentAmount,
            PaymentMethodType methodType,
            Instant now) {
        return new PaymentCompletedEvent(paymentId, orderId, paymentAmount, methodType, now);
    }

    public Long paymentIdValue() {
        return paymentId.value();
    }

    public Long orderIdValue() {
        return orderId.value();
    }
}
