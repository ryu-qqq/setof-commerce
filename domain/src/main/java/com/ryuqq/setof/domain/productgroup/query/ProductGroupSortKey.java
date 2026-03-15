package com.ryuqq.setof.domain.productgroup.query;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * ProductGroup 정렬 키.
 *
 * <p>상품 그룹 목록 조회 시 사용 가능한 정렬 필드를 정의합니다.
 */
public enum ProductGroupSortKey implements SortKey {

    /** 등록일시 순 (기본값) */
    CREATED_AT("createdAt"),

    /** 수정일시 순 */
    UPDATED_AT("updatedAt"),

    /** 상품그룹명 순 */
    NAME("productGroupName"),

    /** 현재가 순 */
    CURRENT_PRICE("currentPrice"),

    /** 할인가 순 */
    SALE_PRICE("salePrice"),

    /** 추천 스코어 순 */
    SCORE("score"),

    /** 리뷰 수 순 */
    REVIEW_COUNT("reviewCount"),

    /** 평균 평점 순 */
    AVERAGE_RATING("averageRating"),

    /** 할인율 순 */
    DISCOUNT_RATE("discountRate");

    private final String fieldName;

    ProductGroupSortKey(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    /** 기본 정렬 키 (등록일시) */
    public static ProductGroupSortKey defaultKey() {
        return CREATED_AT;
    }
}
