package com.ryuqq.setof.application.shippingpolicy.port.in.query;

import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyResponse;

/**
 * 배송 정책 단건 조회 UseCase (Port-In)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetShippingPolicyUseCase {

    /**
     * 배송 정책 단건 조회
     *
     * @param shippingPolicyId 배송 정책 ID
     * @return 배송 정책 응답
     */
    ShippingPolicyResponse execute(Long shippingPolicyId);

    /**
     * 배송 정책 단건 조회 (소유권 검증 포함)
     *
     * @param shippingPolicyId 배송 정책 ID
     * @param sellerId 셀러 ID (소유권 검증용)
     * @return 배송 정책 응답
     * @throws com.ryuqq.setof.domain.shippingpolicy.exception.ShippingPolicyNotOwnerException 소유권
     *     불일치 시
     */
    ShippingPolicyResponse execute(Long shippingPolicyId, Long sellerId);
}
