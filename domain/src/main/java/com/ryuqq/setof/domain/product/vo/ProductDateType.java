package com.ryuqq.setof.domain.product.vo;

/**
 * ProductDateType - 상품 날짜 필터 기준 타입
 *
 * <p>상품 검색 시 날짜 조건을 적용할 필드를 지정합니다.
 *
 * <p><strong>사용 예시:</strong>
 *
 * <pre>{@code
 * // 등록일 기준 검색
 * ProductSearchPeriod period = ProductSearchPeriod.of(
 *     ProductDateType.CREATED_AT,
 *     DateRange.lastDays(7)
 * );
 *
 * // 수정일 기준 검색
 * ProductSearchPeriod period = ProductSearchPeriod.of(
 *     ProductDateType.UPDATED_AT,
 *     DateRange.thisMonth()
 * );
 * }</pre>
 *
 * @author development-team
 * @since 1.0.0
 */
public enum ProductDateType {

    /** 등록일 기준 */
    CREATED_AT("createdAt", "등록일"),

    /** 수정일 기준 */
    UPDATED_AT("updatedAt", "수정일");

    private final String fieldName;
    private final String displayName;

    ProductDateType(String fieldName, String displayName) {
        this.fieldName = fieldName;
        this.displayName = displayName;
    }

    /**
     * 필드명 반환
     *
     * @return 엔티티 필드명
     */
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
}
