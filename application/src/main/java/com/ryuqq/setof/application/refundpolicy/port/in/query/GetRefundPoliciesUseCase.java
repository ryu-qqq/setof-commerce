package com.ryuqq.setof.application.refundpolicy.port.in.query;

import com.ryuqq.setof.application.refundpolicy.dto.query.RefundPolicySearchQuery;
import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyResponse;
import java.util.List;

/**
 * 환불 정책 목록 조회 UseCase (Port-In)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetRefundPoliciesUseCase {

    /**
     * 환불 정책 목록 조회
     *
     * @param query 검색 조건
     * @return 환불 정책 목록
     */
    List<RefundPolicyResponse> execute(RefundPolicySearchQuery query);
}
