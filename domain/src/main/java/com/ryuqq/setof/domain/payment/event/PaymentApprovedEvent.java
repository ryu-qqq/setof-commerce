package com.ryuqq.setof.domain.payment.event;

import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import com.ryuqq.setof.domain.payment.vo.PaymentId;
import com.ryuqq.setof.domain.payment.vo.PaymentMethod;
import com.ryuqq.setof.domain.payment.vo.PaymentMoney;
import com.ryuqq.setof.domain.payment.vo.PgProvider;
import java.time.Instant;

/**
 * PaymentApprovedEvent - 결제 승인 완료 이벤트
 *
 * <p>결제가 성공적으로 승인되었을 때 발행됩니다. 이 이벤트 이후 주문(Order) 생성 로직이 실행됩니다.
 *
 * @param paymentId 결제 ID
 * @param checkoutId 결제 세션 ID
 * @param pgProvider PG사
 * @param pgTransactionId PG 거래 ID
 * @param method 결제 수단
 * @param approvedAmount 승인 금액
 * @param occurredAt 이벤트 발생 시각
 */
public record PaymentApprovedEvent(
        PaymentId paymentId,
        CheckoutId checkoutId,
        PgProvider pgProvider,
        String pgTransactionId,
        PaymentMethod method,
        PaymentMoney approvedAmount,
        Instant occurredAt)
        implements DomainEvent {

    /**
     * Static Factory Method - Payment Aggregate로부터 이벤트 생성
     *
     * @param payment Payment Aggregate
     * @param occurredAt 이벤트 발생 시각
     * @return PaymentApprovedEvent 인스턴스
     */
    public static PaymentApprovedEvent from(Payment payment, Instant occurredAt) {
        return new PaymentApprovedEvent(
                payment.id(),
                payment.checkoutId(),
                payment.pgProvider(),
                payment.pgTransactionId(),
                payment.method(),
                payment.approvedAmount(),
                occurredAt);
    }
}
