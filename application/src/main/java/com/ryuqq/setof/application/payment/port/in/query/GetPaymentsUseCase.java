package com.ryuqq.setof.application.payment.port.in.query;

import com.ryuqq.setof.application.common.response.SliceResponse;
import com.ryuqq.setof.application.payment.dto.query.GetPaymentsQuery;
import com.ryuqq.setof.application.payment.dto.response.PaymentResponse;

/**
 * GetPaymentsUseCase - 결제 목록 조회 UseCase
 *
 * <p>회원의 결제 목록을 커서 기반 페이지네이션으로 조회합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetPaymentsUseCase {

    /**
     * 결제 목록 조회
     *
     * @param query 조회 조건
     * @return Slice 형태의 결제 목록
     */
    SliceResponse<PaymentResponse> getPayments(GetPaymentsQuery query);
}
