package com.ryuqq.setof.application.payment.port.in.query;

import com.ryuqq.setof.application.payment.dto.response.PaymentResponse;

/**
 * 결제 조회 UseCase
 *
 * <p>결제 정보를 조회합니다
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetPaymentUseCase {

    /**
     * 결제 조회
     *
     * @param paymentId 결제 ID (UUID String)
     * @return 결제 응답
     */
    PaymentResponse getPayment(String paymentId);

    /**
     * 체크아웃 ID로 결제 조회
     *
     * @param checkoutId 체크아웃 ID (UUID String)
     * @return 결제 응답
     */
    PaymentResponse getPaymentByCheckoutId(String checkoutId);

    /**
     * Legacy Payment ID로 결제 조회
     *
     * <p>V1 API 호환을 위한 Legacy ID 기반 조회
     *
     * @param legacyPaymentId Legacy Payment ID (Long)
     * @return 결제 응답
     */
    PaymentResponse getPaymentByLegacyId(Long legacyPaymentId);
}
