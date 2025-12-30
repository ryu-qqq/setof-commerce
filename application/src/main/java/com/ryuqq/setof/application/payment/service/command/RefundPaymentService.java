package com.ryuqq.setof.application.payment.service.command;

import com.ryuqq.setof.application.payment.assembler.PaymentAssembler;
import com.ryuqq.setof.application.payment.dto.command.RefundPaymentCommand;
import com.ryuqq.setof.application.payment.dto.response.PaymentResponse;
import com.ryuqq.setof.application.payment.manager.command.PaymentPersistenceManager;
import com.ryuqq.setof.application.payment.manager.query.PaymentReadManager;
import com.ryuqq.setof.application.payment.port.in.command.RefundPaymentUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import com.ryuqq.setof.domain.payment.vo.PaymentMoney;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 결제 환불 Service
 *
 * <p>결제 환불을 처리합니다
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class RefundPaymentService implements RefundPaymentUseCase {

    private final PaymentReadManager paymentReadManager;
    private final PaymentPersistenceManager paymentPersistenceManager;
    private final PaymentAssembler paymentAssembler;
    private final ClockHolder clockHolder;

    public RefundPaymentService(
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
     * 결제 환불
     *
     * @param command 결제 환불 Command
     * @return 환불된 결제 응답
     */
    @Override
    @Transactional
    public PaymentResponse refundPayment(RefundPaymentCommand command) {
        Payment payment = paymentReadManager.findById(command.paymentId());
        PaymentMoney refundAmount = PaymentMoney.of(command.refundAmount());
        Instant now = Instant.now(clockHolder.getClock());

        Payment refundedPayment = payment.refund(refundAmount, now);
        paymentPersistenceManager.persist(refundedPayment);
        return paymentAssembler.toResponse(refundedPayment);
    }
}
