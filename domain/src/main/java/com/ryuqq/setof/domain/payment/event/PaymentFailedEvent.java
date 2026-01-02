package com.ryuqq.setof.domain.payment.event;

import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import com.ryuqq.setof.domain.payment.vo.PaymentId;
import com.ryuqq.setof.domain.payment.vo.PaymentMoney;
import java.time.Instant;

/**
 * PaymentFailedEvent - 결제 실패 이벤트
 *
 * <p>결제가 실패했을 때 발행됩니다. 이 이벤트 이후 재고 예약 해제, 결제 세션 만료 등의 작업이 수행됩니다.
 *
 * @param paymentId 결제 ID
 * @param checkoutId 결제 세션 ID
 * @param requestedAmount 요청 금액
 * @param failureReason 실패 사유
 * @param occurredAt 이벤트 발생 시각
 */
public record PaymentFailedEvent(
        PaymentId paymentId,
        CheckoutId checkoutId,
        PaymentMoney requestedAmount,
        String failureReason,
        Instant occurredAt)
        implements DomainEvent {

    /**
     * Static Factory Method - Payment Aggregate로부터 이벤트 생성
     *
     * @param payment Payment Aggregate
     * @param failureReason 실패 사유
     * @param occurredAt 이벤트 발생 시각
     * @return PaymentFailedEvent 인스턴스
     */
    public static PaymentFailedEvent from(
            Payment payment, String failureReason, Instant occurredAt) {
        return new PaymentFailedEvent(
                payment.id(),
                payment.checkoutId(),
                payment.requestedAmount(),
                failureReason,
                occurredAt);
    }
}
