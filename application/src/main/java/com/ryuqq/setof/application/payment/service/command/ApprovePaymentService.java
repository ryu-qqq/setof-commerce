package com.ryuqq.setof.application.payment.service.command;

import com.ryuqq.setof.application.payment.assembler.PaymentAssembler;
import com.ryuqq.setof.application.payment.dto.command.ApprovePaymentCommand;
import com.ryuqq.setof.application.payment.dto.response.PaymentResponse;
import com.ryuqq.setof.application.payment.manager.command.PaymentPersistenceManager;
import com.ryuqq.setof.application.payment.manager.query.PaymentReadManager;
import com.ryuqq.setof.application.payment.port.in.command.ApprovePaymentUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import com.ryuqq.setof.domain.payment.vo.PaymentMoney;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 결제 승인 Service
 *
 * <p>PG사로부터 결제 승인을 처리합니다
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class ApprovePaymentService implements ApprovePaymentUseCase {

    private final PaymentReadManager paymentReadManager;
    private final PaymentPersistenceManager paymentPersistenceManager;
    private final PaymentAssembler paymentAssembler;
    private final ClockHolder clockHolder;

    public ApprovePaymentService(
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
     * 결제 승인
     *
     * @param command 결제 승인 Command
     * @return 승인된 결제 응답
     */
    @Override
    @Transactional
    public PaymentResponse approvePayment(ApprovePaymentCommand command) {
        Payment payment = paymentReadManager.findById(command.paymentId());
        PaymentMoney approvedAmount = PaymentMoney.of(command.approvedAmount());
        Instant now = Instant.now(clockHolder.getClock());

        Payment approvedPayment = payment.approve(command.pgTransactionId(), approvedAmount, now);
        paymentPersistenceManager.persist(approvedPayment);
        return paymentAssembler.toResponse(approvedPayment);
    }
}
