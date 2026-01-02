package com.ryuqq.setof.application.shippingpolicy.port.in.query;

import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyResponse;

/**
 * 셀러의 기본 배송 정책 단건 조회 UseCase (Port-In)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetDefaultShippingAddressUseCase {

    /**
     * 셀러의 기본 배송 정책 단건 조회
     *
     * @param sellerId 셀러 ID
     * @return 환불 정책 응답
     */
    ShippingPolicyResponse execute(Long sellerId);
}
