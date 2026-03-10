package com.ryuqq.setof.application.payment.port.out.command;

import com.ryuqq.setof.domain.payment.aggregate.Payment;

/**
 * 결제 명령 Port.
 *
 * <p>Payment 도메인 객체를 영속화합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface PaymentCommandPort {

    /**
     * 결제를 영속화합니다.
     *
     * @param payment 결제 도메인 객체
     * @return 영속화 결과 (paymentId, paymentUniqueId)
     */
    PaymentCommandResult persist(Payment payment);

    /**
     * 결제 영속화 결과.
     *
     * @param paymentId 생성된 결제 ID
     * @param paymentUniqueId PG 결제용 고유 ID
     */
    record PaymentCommandResult(long paymentId, String paymentUniqueId) {}
}
