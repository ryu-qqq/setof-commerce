package com.ryuqq.setof.domain.product.vo;

/**
 * ProductSortType - 상품 정렬 기준 타입
 *
 * <p>상품 목록 조회 시 정렬 기준을 지정합니다.
 *
 * <p><strong>사용 예시:</strong>
 *
 * <pre>{@code
 * // 최신순 정렬
 * ProductGroupSearchCriteria criteria = ProductGroupSearchCriteria.builder()
 *     .sortType(ProductSortType.LATEST)
 *     .build();
 *
 * // 가격순 정렬
 * ProductGroupSearchCriteria criteria = ProductGroupSearchCriteria.builder()
 *     .sortType(ProductSortType.PRICE_ASC)
 *     .build();
 * }</pre>
 *
 * @author development-team
 * @since 1.0.0
 */
public enum ProductSortType {

    /** 최신순 (등록일 내림차순) */
    LATEST("createdAt", "DESC", "최신순"),

    /** 가격 낮은순 */
    PRICE_ASC("price", "ASC", "가격 낮은순"),

    /** 가격 높은순 */
    PRICE_DESC("price", "DESC", "가격 높은순");

    // NONE("", "", DESC),
    // RECOMMEND("score", "score", DESC),
    // REVIEW("reviewCount", "review_count", DESC),
    // RECENT("updateDate", "update_date", DESC),
    // HIGH_RATING("averageRating", "average_rating", DESC),
    // LOW_PRICE("productGroupDetails.price.salePrice", "sale_price", ASC),
    // HIGH_PRICE("productGroupDetails.price.salePrice", "sale_price", DESC),
    // LOW_DISCOUNT("productGroupDetails.price.discountRate", "discount_rate", ASC),
    // HIGH_DISCOUNT("productGroupDetails.price.discountRate", "discount_rate", DESC);

    private final String fieldName;
    private final String direction;
    private final String displayName;

    ProductSortType(String fieldName, String direction, String displayName) {
        this.fieldName = fieldName;
        this.direction = direction;
        this.displayName = displayName;
    }

    /**
     * 정렬 필드명 반환
     *
     * @return 필드명
     */
    public String fieldName() {
        return fieldName;
    }

    /**
     * 정렬 방향 반환
     *
     * @return ASC 또는 DESC
     */
    public String direction() {
        return direction;
    }

    /**
     * 화면 표시명 반환
     *
     * @return 표시명
     */
    public String displayName() {
        return displayName;
    }

    /**
     * 오름차순 여부 확인
     *
     * @return ASC이면 true
     */
    public boolean isAscending() {
        return "ASC".equals(direction);
    }

    /**
     * 내림차순 여부 확인
     *
     * @return DESC이면 true
     */
    public boolean isDescending() {
        return "DESC".equals(direction);
    }

    /**
     * 기본 정렬 기준 반환
     *
     * @return LATEST (최신순)
     */
    public static ProductSortType defaultSort() {
        return LATEST;
    }
}
