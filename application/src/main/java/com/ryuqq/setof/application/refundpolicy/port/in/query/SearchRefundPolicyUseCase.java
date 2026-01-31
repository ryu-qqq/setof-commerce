package com.ryuqq.setof.application.refundpolicy.port.in.query;

import com.ryuqq.setof.application.refundpolicy.dto.query.RefundPolicySearchParams;
import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyPageResult;

/**
 * 환불정책 검색 UseCase.
 *
 * <p>APP-ASM-001: RefundPolicyPageResult로 페이징 결과 반환
 */
public interface SearchRefundPolicyUseCase {

    RefundPolicyPageResult execute(RefundPolicySearchParams params);
}
