package com.ryuqq.setof.application.payment.port.in.query;

import com.ryuqq.setof.application.payment.dto.query.PaymentSearchParams;
import com.ryuqq.setof.application.payment.dto.response.PaymentSliceResult;

/**
 * GetPaymentsUseCase - 결제 목록 조회 UseCase.
 *
 * <p>APP-UC-001: UseCase = 1 behavior.
 *
 * <p>APP-UC-002: UseCase 네이밍 규칙 - Get*UseCase.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetPaymentsUseCase {

    /**
     * 결제 목록 조회 (커서 페이징 + 상태 필터).
     *
     * @param params 결제 검색 파라미터
     * @return 결제 슬라이스 결과
     */
    PaymentSliceResult execute(PaymentSearchParams params);
}
