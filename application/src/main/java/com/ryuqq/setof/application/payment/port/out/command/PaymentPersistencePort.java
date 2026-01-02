package com.ryuqq.setof.application.payment.port.out.command;

import com.ryuqq.setof.domain.payment.aggregate.Payment;
import com.ryuqq.setof.domain.payment.vo.PaymentId;

/**
 * Payment Persistence Port (Command)
 *
 * <p>Payment Aggregate를 영속화하는 쓰기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface PaymentPersistencePort {

    /**
     * Payment 저장 (신규 생성 또는 수정)
     *
     * @param payment 저장할 Payment (Domain Aggregate)
     * @return 저장된 Payment의 ID
     */
    PaymentId persist(Payment payment);
}
