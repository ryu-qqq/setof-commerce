package com.ryuqq.setof.application.refundpolicy.port.in.query;

import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyResponse;

/**
 * 환불 정책 단건 조회 UseCase (Port-In)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetRefundPolicyUseCase {

    /**
     * 환불 정책 단건 조회
     *
     * @param refundPolicyId 환불 정책 ID
     * @return 환불 정책 응답
     */
    RefundPolicyResponse execute(Long refundPolicyId);

    /**
     * 환불 정책 단건 조회 (소유권 검증 포함)
     *
     * @param refundPolicyId 환불 정책 ID
     * @param sellerId 셀러 ID (소유권 검증용)
     * @return 환불 정책 응답
     * @throws com.ryuqq.setof.domain.refundpolicy.exception.RefundPolicyNotOwnerException 소유권 불일치 시
     */
    RefundPolicyResponse execute(Long refundPolicyId, Long sellerId);
}
