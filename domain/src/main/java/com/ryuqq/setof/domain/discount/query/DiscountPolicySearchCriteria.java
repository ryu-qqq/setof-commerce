package com.ryuqq.setof.domain.discount.query;

import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.discount.vo.ApplicationType;
import com.ryuqq.setof.domain.discount.vo.PublisherType;
import com.ryuqq.setof.domain.discount.vo.StackingGroup;
import com.ryuqq.setof.domain.seller.id.SellerId;

/**
 * DiscountPolicy 검색 조건 Criteria.
 *
 * <p>할인 정책 목록 조회 시 사용하는 검색 조건과 페이징 정보를 정의합니다.
 *
 * @param sellerId 셀러 ID (nullable, null이면 전체)
 * @param applicationType 적용 방식 필터 (nullable)
 * @param publisherType 생성 주체 필터 (nullable)
 * @param stackingGroup 스태킹 그룹 필터 (nullable)
 * @param activeOnly 활성 정책만 조회 여부
 * @param queryContext 정렬 및 페이징 정보
 */
public record DiscountPolicySearchCriteria(
        SellerId sellerId,
        ApplicationType applicationType,
        PublisherType publisherType,
        StackingGroup stackingGroup,
        boolean activeOnly,
        QueryContext<DiscountPolicySortKey> queryContext) {

    public static DiscountPolicySearchCriteria of(
            SellerId sellerId,
            ApplicationType applicationType,
            PublisherType publisherType,
            StackingGroup stackingGroup,
            boolean activeOnly,
            QueryContext<DiscountPolicySortKey> queryContext) {
        return new DiscountPolicySearchCriteria(
                sellerId, applicationType, publisherType, stackingGroup, activeOnly, queryContext);
    }

    /** 기본 검색 조건 (활성 정책, 등록순 내림차순) */
    public static DiscountPolicySearchCriteria defaultCriteria() {
        return new DiscountPolicySearchCriteria(
                null,
                null,
                null,
                null,
                true,
                QueryContext.defaultOf(DiscountPolicySortKey.defaultKey()));
    }

    /** 셀러별 검색 조건 */
    public static DiscountPolicySearchCriteria forSeller(SellerId sellerId) {
        return new DiscountPolicySearchCriteria(
                sellerId,
                null,
                PublisherType.SELLER,
                null,
                true,
                QueryContext.defaultOf(DiscountPolicySortKey.defaultKey()));
    }

    /** 셀러 ID 값 반환 (편의 메서드) */
    public Long sellerIdValue() {
        return sellerId != null ? sellerId.value() : null;
    }

    /** 페이지 크기 반환 (편의 메서드) */
    public int size() {
        return queryContext.size();
    }

    /** 오프셋 반환 (편의 메서드) */
    public long offset() {
        return queryContext.offset();
    }

    /** 현재 페이지 번호 반환 (편의 메서드) */
    public int page() {
        return queryContext.page();
    }
}
