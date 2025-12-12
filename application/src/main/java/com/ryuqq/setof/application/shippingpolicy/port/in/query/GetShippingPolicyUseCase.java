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
}
