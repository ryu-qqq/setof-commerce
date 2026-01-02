package com.ryuqq.setof.application.payment.service.command;

import com.ryuqq.setof.application.payment.assembler.PaymentAssembler;
import com.ryuqq.setof.application.payment.dto.response.PaymentResponse;
import com.ryuqq.setof.application.payment.manager.command.PaymentPersistenceManager;
import com.ryuqq.setof.application.payment.manager.query.PaymentReadManager;
import com.ryuqq.setof.application.payment.port.in.command.CancelPaymentUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 결제 취소 Service
 *
 * <p>결제를 취소합니다
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class CancelPaymentService implements CancelPaymentUseCase {

    private final PaymentReadManager paymentReadManager;
    private final PaymentPersistenceManager paymentPersistenceManager;
    private final PaymentAssembler paymentAssembler;
    private final ClockHolder clockHolder;

    public CancelPaymentService(
            PaymentReadManager paymentReadManager,
            PaymentPersistenceManager paymentPersistenceManager,
            PaymentAssembler paymentAssembler,
            ClockHolder clockHolder) {
        this.paymentReadManager = paymentReadManager;
        this.paymentPersistenceManager = paymentPersistenceManager;
        this.paymentAssembler = paymentAssembler;
        this.clockHolder = clockHolder;
    }

    /**
     * 결제 취소
     *
     * @param paymentId 결제 ID (UUID String)
     * @return 취소된 결제 응답
     */
    @Override
    @Transactional
    public PaymentResponse cancelPayment(String paymentId) {
        Payment payment = paymentReadManager.findById(paymentId);
        Instant now = Instant.now(clockHolder.getClock());

        Payment cancelledPayment = payment.cancel(now);
        paymentPersistenceManager.persist(cancelledPayment);
        return paymentAssembler.toResponse(cancelledPayment);
    }
}
