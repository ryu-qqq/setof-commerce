package com.ryuqq.setof.application.discount.port.in.query;

import com.ryuqq.setof.application.discount.dto.query.DiscountPolicySearchParams;
import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyPageResult;

/**
 * 할인 정책 목록 검색 UseCase.
 *
 * <p>다양한 조건과 페이징으로 할인 정책 목록을 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface SearchDiscountPoliciesUseCase {

    /**
     * 검색 조건으로 할인 정책 목록을 페이징 조회합니다.
     *
     * @param params 검색 파라미터 (조건 필터 + 페이징 정보)
     * @return 할인 정책 페이지 결과 DTO
     */
    DiscountPolicyPageResult execute(DiscountPolicySearchParams params);
}
