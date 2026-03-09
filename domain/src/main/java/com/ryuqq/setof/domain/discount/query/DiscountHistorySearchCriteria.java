package com.ryuqq.setof.domain.discount.query;

import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.discount.id.DiscountPolicyId;
import com.ryuqq.setof.domain.discount.vo.DiscountChangeType;

/**
 * DiscountPolicyHistory 검색 조건 Criteria.
 *
 * <p>할인 정책 변경 이력 조회 시 사용하는 검색 조건과 페이징 정보를 정의합니다.
 *
 * @param policyId 정책 ID (nullable, null이면 전체)
 * @param changeType 변경 유형 필터 (nullable)
 * @param operatorId 변경 관리자 ID (nullable)
 * @param queryContext 정렬 및 페이징 정보
 */
public record DiscountHistorySearchCriteria(
        DiscountPolicyId policyId,
        DiscountChangeType changeType,
        Long operatorId,
        QueryContext<DiscountHistorySortKey> queryContext) {

    public static DiscountHistorySearchCriteria of(
            DiscountPolicyId policyId,
            DiscountChangeType changeType,
            Long operatorId,
            QueryContext<DiscountHistorySortKey> queryContext) {
        return new DiscountHistorySearchCriteria(policyId, changeType, operatorId, queryContext);
    }

    /** 특정 정책의 전체 이력 조회 */
    public static DiscountHistorySearchCriteria forPolicy(DiscountPolicyId policyId) {
        return new DiscountHistorySearchCriteria(
                policyId, null, null, QueryContext.defaultOf(DiscountHistorySortKey.defaultKey()));
    }

    /** 기본 검색 조건 (전체 이력, 변경일시 내림차순) */
    public static DiscountHistorySearchCriteria defaultCriteria() {
        return new DiscountHistorySearchCriteria(
                null, null, null, QueryContext.defaultOf(DiscountHistorySortKey.defaultKey()));
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
