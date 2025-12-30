package com.ryuqq.setof.application.payment.port.in.command;

import com.ryuqq.setof.application.payment.dto.response.PaymentResponse;

/**
 * 결제 취소 UseCase
 *
 * <p>결제를 취소합니다
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CancelPaymentUseCase {

    /**
     * 결제 취소
     *
     * @param paymentId 결제 ID (UUID String)
     * @return 취소된 결제 응답
     */
    PaymentResponse cancelPayment(String paymentId);
}
