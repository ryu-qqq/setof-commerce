package com.ryuqq.setof.domain.payment.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * PaymentCompletionInProgressException - 결제 완료 처리 중 예외
 *
 * <p>동일한 결제에 대해 이미 완료 처리가 진행 중인 경우 발생합니다. (프론트엔드 콜백과 PG 웹훅 간의 경쟁 조건 방지)
 */
public class PaymentCompletionInProgressException extends DomainException {

    /**
     * 결제 완료 처리 중 예외 생성
     *
     * @param paymentId 결제 ID
     * @return PaymentCompletionInProgressException 인스턴스
     */
    public static PaymentCompletionInProgressException forPayment(String paymentId) {
        return new PaymentCompletionInProgressException(paymentId);
    }

    private PaymentCompletionInProgressException(String paymentId) {
        super(
                PaymentErrorCode.PAYMENT_COMPLETION_IN_PROGRESS,
                String.format("이미 결제 완료 처리 중입니다 - paymentId: %s", paymentId),
                Map.of("paymentId", paymentId));
    }
}
