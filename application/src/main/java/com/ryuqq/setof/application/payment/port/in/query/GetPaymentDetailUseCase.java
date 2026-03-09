package com.ryuqq.setof.application.payment.port.in.query;

import com.ryuqq.setof.application.payment.dto.response.PaymentDetailResult;

/**
 * GetPaymentDetailUseCase - 결제 단건 상세 조회 UseCase.
 *
 * <p>APP-UC-001: UseCase = 1 behavior.
 *
 * <p>APP-UC-002: UseCase 네이밍 규칙 - Get*UseCase.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetPaymentDetailUseCase {

    /**
     * 결제 단건 상세 조회.
     *
     * @param paymentId 결제 ID
     * @param userId 사용자 ID (본인 확인용)
     * @return 결제 전체 상세 결과
     */
    PaymentDetailResult execute(long paymentId, long userId);
}
