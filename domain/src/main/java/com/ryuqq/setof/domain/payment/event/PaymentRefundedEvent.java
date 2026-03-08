package com.ryuqq.setof.domain.payment.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import com.ryuqq.setof.domain.payment.id.PaymentId;
import java.time.Instant;

/**
 * 결제 환불 이벤트.
 *
 * <p>결제가 부분 환불 또는 전액 환불되었을 때 발행됩니다.
 *
 * @param paymentId 결제 ID
 * @param orderId 주문 ID
 * @param refundAmount 환불 금액
 * @param isFullRefund 전액 환불 여부
 * @param occurredAt 이벤트 발생 시각
 */
public record PaymentRefundedEvent(
        PaymentId paymentId,
        LegacyOrderId orderId,
        Money refundAmount,
        boolean isFullRefund,
        Instant occurredAt)
        implements DomainEvent {

    public static PaymentRefundedEvent of(
            PaymentId paymentId,
            LegacyOrderId orderId,
            Money refundAmount,
            boolean isFullRefund,
            Instant now) {
        return new PaymentRefundedEvent(paymentId, orderId, refundAmount, isFullRefund, now);
    }

    public Long paymentIdValue() {
        return paymentId.value();
    }

    public Long orderIdValue() {
        return orderId.value();
    }
}
