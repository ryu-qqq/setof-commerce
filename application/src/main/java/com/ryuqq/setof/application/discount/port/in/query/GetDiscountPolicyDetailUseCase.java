package com.ryuqq.setof.application.discount.port.in.query;

import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyResult;

/**
 * 할인 정책 단건 조회 UseCase.
 *
 * <p>ID로 특정 할인 정책의 상세 정보를 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetDiscountPolicyDetailUseCase {

    /**
     * 할인 정책 단건을 조회합니다.
     *
     * @param discountPolicyId 조회할 할인 정책 ID
     * @return 할인 정책 결과 DTO
     */
    DiscountPolicyResult execute(long discountPolicyId);
}
