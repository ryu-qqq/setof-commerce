package com.ryuqq.setof.domain.product.vo;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * ProductSortBy - 상품 정렬 필드 Value Object
 *
 * <p>상품 목록 조회 시 정렬 기준 필드를 지정합니다. 정렬 방향(ASC/DESC)은 별도의 SortDirection을 사용합니다.
 *
 * <p><strong>사용 예시:</strong>
 *
 * <pre>{@code
 * // 가격순 내림차순 정렬
 * ProductGroupSearchCriteria criteria = ProductGroupSearchCriteria.builder()
 *     .sortBy(ProductSortBy.PRICE)
 *     .sortDirection(SortDirection.DESC)
 *     .build();
 *
 * // 등록일순 오름차순 정렬
 * ProductGroupSearchCriteria criteria = ProductGroupSearchCriteria.builder()
 *     .sortBy(ProductSortBy.CREATED_AT)
 *     .sortDirection(SortDirection.ASC)
 *     .build();
 * }</pre>
 *
 * @author development-team
 * @since 2.0.0
 */
public enum ProductSortBy implements SortKey {

    /** ID 기준 정렬 */
    ID("id", "ID순"),

    /** 가격 기준 정렬 */
    PRICE("price", "가격순"),

    /** 등록일 기준 정렬 */
    CREATED_AT("createdAt", "등록일순"),

    /** 수정일 기준 정렬 */
    UPDATED_AT("updatedAt", "수정일순"),

    /** 상품명 기준 정렬 */
    NAME("name", "상품명순");

    private final String fieldName;
    private final String displayName;

    ProductSortBy(String fieldName, String displayName) {
        this.fieldName = fieldName;
        this.displayName = displayName;
    }

    @Override
    public String fieldName() {
        return fieldName;
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
     * 기본 정렬 필드 반환
     *
     * @return CREATED_AT (등록일)
     */
    public static ProductSortBy defaultSortBy() {
        return CREATED_AT;
    }

    /**
     * 문자열로부터 ProductSortBy 파싱 (대소문자 무관)
     *
     * @param value 문자열 (예: "price", "PRICE", "created_at", "CREATED_AT")
     * @return ProductSortBy (null이거나 유효하지 않으면 기본값 반환)
     */
    public static ProductSortBy fromString(String value) {
        if (value == null || value.isBlank()) {
            return defaultSortBy();
        }
        try {
            String normalized = value.toUpperCase().trim().replace("-", "_");
            return ProductSortBy.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            return defaultSortBy();
        }
    }
}
