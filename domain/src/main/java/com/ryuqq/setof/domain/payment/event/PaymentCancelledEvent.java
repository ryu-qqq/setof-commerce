package com.ryuqq.setof.domain.payment.event;

import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import com.ryuqq.setof.domain.payment.vo.PaymentId;
import com.ryuqq.setof.domain.payment.vo.PaymentMoney;
import java.time.Instant;

/**
 * PaymentCancelledEvent - 결제 취소 이벤트
 *
 * <p>결제가 취소되었을 때 발행됩니다.
 *
 * @param paymentId 결제 ID
 * @param checkoutId 결제 세션 ID
 * @param cancelledAmount 취소 금액
 * @param occurredAt 이벤트 발생 시각
 */
public record PaymentCancelledEvent(
        PaymentId paymentId,
        CheckoutId checkoutId,
        PaymentMoney cancelledAmount,
        Instant occurredAt)
        implements DomainEvent {

    /**
     * Static Factory Method - Payment Aggregate로부터 이벤트 생성
     *
     * @param payment Payment Aggregate
     * @param occurredAt 이벤트 발생 시각
     * @return PaymentCancelledEvent 인스턴스
     */
    public static PaymentCancelledEvent from(Payment payment, Instant occurredAt) {
        return new PaymentCancelledEvent(
                payment.id(), payment.checkoutId(), payment.approvedAmount(), occurredAt);
    }
}
