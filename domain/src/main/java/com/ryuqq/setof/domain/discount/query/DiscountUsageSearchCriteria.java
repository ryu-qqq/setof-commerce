package com.ryuqq.setof.domain.discount.query;

import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.discount.id.DiscountPolicyId;

/**
 * DiscountUsage 검색 조건 Criteria.
 *
 * <p>할인 사용 이력 조회 시 사용하는 검색 조건과 페이징 정보를 정의합니다.
 *
 * @param policyId 정책 ID (nullable, null이면 전체)
 * @param memberId 회원 ID (nullable)
 * @param orderId 주문 ID (nullable)
 * @param queryContext 정렬 및 페이징 정보
 */
public record DiscountUsageSearchCriteria(
        DiscountPolicyId policyId,
        Long memberId,
        Long orderId,
        QueryContext<DiscountUsageSortKey> queryContext) {

    public static DiscountUsageSearchCriteria of(
            DiscountPolicyId policyId,
            Long memberId,
            Long orderId,
            QueryContext<DiscountUsageSortKey> queryContext) {
        return new DiscountUsageSearchCriteria(policyId, memberId, orderId, queryContext);
    }

    /** 특정 정책의 사용 이력 조회 */
    public static DiscountUsageSearchCriteria forPolicy(DiscountPolicyId policyId) {
        return new DiscountUsageSearchCriteria(
                policyId, null, null, QueryContext.defaultOf(DiscountUsageSortKey.defaultKey()));
    }

    /** 기본 검색 조건 (전체 사용 이력, 사용일시 내림차순) */
    public static DiscountUsageSearchCriteria defaultCriteria() {
        return new DiscountUsageSearchCriteria(
                null, null, null, QueryContext.defaultOf(DiscountUsageSortKey.defaultKey()));
    }

    /** 정책 ID 값 반환 (편의 메서드) */
    public Long policyIdValue() {
        return policyId != null ? policyId.value() : null;
    }

    public int size() {
        return queryContext.size();
    }

    public long offset() {
        return queryContext.offset();
    }

    public int page() {
        return queryContext.page();
    }
}
