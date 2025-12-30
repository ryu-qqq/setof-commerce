package com.ryuqq.setof.application.payment.port.in.command;

import com.ryuqq.setof.application.payment.dto.command.RefundPaymentCommand;
import com.ryuqq.setof.application.payment.dto.response.PaymentResponse;

/**
 * 결제 환불 UseCase
 *
 * <p>결제 환불을 처리합니다
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RefundPaymentUseCase {

    /**
     * 결제 환불
     *
     * @param command 결제 환불 Command
     * @return 환불된 결제 응답
     */
    PaymentResponse refundPayment(RefundPaymentCommand command);
}
