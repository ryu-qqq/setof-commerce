package com.ryuqq.setof.domain.productgroup.query;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * ProductGroup 정렬 키.
 *
 * <p>상품 그룹 목록 조회 시 사용 가능한 정렬 필드를 정의합니다.
 *
 * <p>레거시 orderType(RECOMMEND, REVIEW 등)과의 호환을 위해 score 기반 정렬 키도 포함합니다.
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

    /** 추천순 (score 기반) */
    RECOMMEND("score"),

    /** 리뷰순 */
    REVIEW("reviewCount"),

    /** 평점순 */
    RATING("rating"),

    /** 할인율순 */
    DISCOUNT("discountRate"),

    /** 최신순 */
    RECENT("createdAt");

    private final String fieldName;

    ProductGroupSortKey(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    /** 기본 정렬 키 (추천순) */
    public static ProductGroupSortKey defaultKey() {
        return RECOMMEND;
    }

    /**
     * 레거시 orderType 문자열로부터 변환.
     *
     * @param orderType 레거시 정렬 타입 (RECOMMEND, LOW_PRICE, HIGH_PRICE 등)
     * @return 대응하는 ProductGroupSortKey
     */
    public static ProductGroupSortKey fromLegacyOrderType(String orderType) {
        if (orderType == null || orderType.isBlank()) {
            return RECOMMEND;
        }
        return switch (orderType.toUpperCase()) {
            case "RECOMMEND" -> RECOMMEND;
            case "REVIEW" -> REVIEW;
            case "HIGH_RATING" -> RATING;
            case "LOW_PRICE", "HIGH_PRICE" -> CURRENT_PRICE;
            case "LOW_DISCOUNT", "HIGH_DISCOUNT" -> DISCOUNT;
            case "RECENT" -> RECENT;
            default -> RECOMMEND;
        };
    }

    /**
     * 레거시 orderType에 따른 정렬 방향 결정.
     *
     * @param orderType 레거시 정렬 타입
     * @return 오름차순이면 true
     */
    public static boolean isAscendingForLegacy(String orderType) {
        if (orderType == null) {
            return false;
        }
        return switch (orderType.toUpperCase()) {
            case "LOW_PRICE", "LOW_DISCOUNT" -> true;
            default -> false;
        };
    }
}
