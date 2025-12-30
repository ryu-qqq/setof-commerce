package com.ryuqq.setof.application.payment.manager.command;

import com.ryuqq.setof.application.payment.port.out.command.PaymentPersistencePort;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import com.ryuqq.setof.domain.payment.vo.PaymentId;
import org.springframework.stereotype.Component;

/**
 * Payment Persistence Manager
 *
 * <p>Payment 영속화 관리자
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class PaymentPersistenceManager {

    private final PaymentPersistencePort paymentPersistencePort;

    public PaymentPersistenceManager(PaymentPersistencePort paymentPersistencePort) {
        this.paymentPersistencePort = paymentPersistencePort;
    }

    /**
     * Payment 저장
     *
     * @param payment 저장할 Payment
     * @return 저장된 Payment ID
     */
    public PaymentId persist(Payment payment) {
        return paymentPersistencePort.persist(payment);
    }
}
