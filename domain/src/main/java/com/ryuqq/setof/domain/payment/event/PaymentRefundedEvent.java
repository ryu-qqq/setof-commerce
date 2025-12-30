package com.ryuqq.setof.domain.payment.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import com.ryuqq.setof.domain.payment.vo.PaymentId;
import com.ryuqq.setof.domain.payment.vo.PaymentMoney;
import com.ryuqq.setof.domain.payment.vo.PaymentStatus;
import java.time.Instant;

/**
 * PaymentRefundedEvent - 결제 환불 이벤트
 *
 * <p>결제가 환불되었을 때 발행됩니다. 부분 환불과 전액 환불 모두 이 이벤트를 사용합니다.
 *
 * @param paymentId 결제 ID
 * @param refundedAmount 이번 환불 금액
 * @param totalRefundedAmount 누적 환불 금액
 * @param remainingAmount 잔여 금액
 * @param status 환불 후 상태 (PARTIAL_REFUNDED 또는 FULLY_REFUNDED)
 * @param occurredAt 이벤트 발생 시각
 */
public record PaymentRefundedEvent(
        PaymentId paymentId,
        PaymentMoney refundedAmount,
        PaymentMoney totalRefundedAmount,
        PaymentMoney remainingAmount,
        PaymentStatus status,
        Instant occurredAt)
        implements DomainEvent {

    /**
     * Static Factory Method - Payment Aggregate로부터 이벤트 생성
     *
     * @param payment Payment Aggregate (환불 처리 후 상태)
     * @param refundedAmount 이번 환불 금액
     * @param occurredAt 이벤트 발생 시각
     * @return PaymentRefundedEvent 인스턴스
     */
    public static PaymentRefundedEvent from(
            Payment payment, PaymentMoney refundedAmount, Instant occurredAt) {
        return new PaymentRefundedEvent(
                payment.id(),
                refundedAmount,
                payment.refundedAmount(),
                payment.refundableAmount(),
                payment.status(),
                occurredAt);
    }
}
