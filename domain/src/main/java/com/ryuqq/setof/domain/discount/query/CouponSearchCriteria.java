package com.ryuqq.setof.domain.discount.query;

import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.discount.vo.CouponType;

/**
 * Coupon 검색 조건 Criteria.
 *
 * <p>쿠폰 목록 조회 시 사용하는 검색 조건과 페이징 정보를 정의합니다.
 *
 * @param couponType 쿠폰 발급 방식 필터 (nullable)
 * @param activeOnly 활성 쿠폰만 조회 여부
 * @param queryContext 정렬 및 페이징 정보
 */
public record CouponSearchCriteria(
        CouponType couponType, boolean activeOnly, QueryContext<CouponSortKey> queryContext) {

    public static CouponSearchCriteria of(
            CouponType couponType, boolean activeOnly, QueryContext<CouponSortKey> queryContext) {
        return new CouponSearchCriteria(couponType, activeOnly, queryContext);
    }

    /** 기본 검색 조건 (활성 쿠폰, 등록순 내림차순) */
    public static CouponSearchCriteria defaultCriteria() {
        return new CouponSearchCriteria(
                null, true, QueryContext.defaultOf(CouponSortKey.defaultKey()));
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
