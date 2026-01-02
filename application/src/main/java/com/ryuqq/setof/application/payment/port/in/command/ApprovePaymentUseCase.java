package com.ryuqq.setof.application.payment.port.in.command;

import com.ryuqq.setof.application.payment.dto.command.ApprovePaymentCommand;
import com.ryuqq.setof.application.payment.dto.response.PaymentResponse;

/**
 * 결제 승인 UseCase
 *
 * <p>PG사로부터 결제 승인을 처리합니다
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ApprovePaymentUseCase {

    /**
     * 결제 승인
     *
     * @param command 결제 승인 Command
     * @return 승인된 결제 응답
     */
    PaymentResponse approvePayment(ApprovePaymentCommand command);
}
