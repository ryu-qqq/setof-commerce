package com.ryuqq.setof.application.refundpolicy.port.in.query;

import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyResponse;

/**
 * 셀러의 기본 환불 정책 단건 조회 UseCase (Port-In)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetDefaultRefundPolicyUseCase {

    /**
     * 셀러의 기본 환불 정책 단건 조회
     *
     * @param sellerId 셀러 ID
     * @return 환불 정책 응답
     */
    RefundPolicyResponse execute(Long sellerId);
}
