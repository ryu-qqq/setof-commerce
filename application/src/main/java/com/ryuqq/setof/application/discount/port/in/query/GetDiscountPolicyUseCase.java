package com.ryuqq.setof.application.discount.port.in.query;

import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyResponse;

/**
 * 할인 정책 단건 조회 UseCase (Port-In)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetDiscountPolicyUseCase {

    /**
     * 할인 정책 단건 조회
     *
     * @param discountPolicyId 할인 정책 ID
     * @return 할인 정책 응답 DTO
     */
    DiscountPolicyResponse execute(Long discountPolicyId);
}
