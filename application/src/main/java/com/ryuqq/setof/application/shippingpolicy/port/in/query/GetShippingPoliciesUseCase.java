package com.ryuqq.setof.application.shippingpolicy.port.in.query;

import com.ryuqq.setof.application.shippingpolicy.dto.query.ShippingPolicySearchQuery;
import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyResponse;
import java.util.List;

/**
 * 배송 정책 목록 조회 UseCase (Port-In)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetShippingPoliciesUseCase {

    /**
     * 배송 정책 목록 조회
     *
     * @param query 검색 조건
     * @return 배송 정책 목록
     */
    List<ShippingPolicyResponse> execute(ShippingPolicySearchQuery query);
}
