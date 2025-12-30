package com.ryuqq.setof.application.discount.port.in.query;

import com.ryuqq.setof.application.discount.dto.query.DiscountPolicySearchQuery;
import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyResponse;
import java.util.List;

/**
 * 할인 정책 목록 조회 UseCase (Port-In)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetDiscountPoliciesUseCase {

    /**
     * 할인 정책 목록 조회
     *
     * @param query 검색 조건
     * @return 할인 정책 응답 DTO 목록
     */
    List<DiscountPolicyResponse> execute(DiscountPolicySearchQuery query);
}
