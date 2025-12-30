package com.ryuqq.setof.domain.seller.vo;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * SellerSortBy - 셀러 정렬 필드 Value Object
 *
 * <p>셀러 목록 조회 시 정렬 기준 필드를 지정합니다. 정렬 방향(ASC/DESC)은 별도의 SortDirection을 사용합니다.
 *
 * <p><strong>사용 예시:</strong>
 *
 * <pre>{@code
 * // 등록일순 내림차순 정렬
 * SellerSearchCriteria criteria = SellerSearchCriteria.builder()
 *     .sortBy(SellerSortBy.CREATED_AT)
 *     .sortDirection(SortDirection.DESC)
 *     .build();
 * }</pre>
 *
 * @author development-team
 * @since 1.0.0
 */
public enum SellerSortBy implements SortKey {

    /** 셀러 ID 기준 정렬 */
    ID("id", "ID순"),

    /** 셀러명 기준 정렬 */
    SELLER_NAME("sellerName", "셀러명순"),

    /** 등록일 기준 정렬 */
    CREATED_AT("createdAt", "등록일순"),

    /** 수정일 기준 정렬 */
    UPDATED_AT("updatedAt", "수정일순"),

    /** 승인일 기준 정렬 */
    APPROVED_AT("approvedAt", "승인일순");

    private final String fieldName;
    private final String displayName;

    SellerSortBy(String fieldName, String displayName) {
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
    public static SellerSortBy defaultSortBy() {
        return CREATED_AT;
    }

    /**
     * 문자열로부터 SellerSortBy 파싱 (대소문자 무관)
     *
     * @param value 문자열 (예: "seller_name", "SELLER_NAME", "sellerName")
     * @return SellerSortBy (null이거나 유효하지 않으면 기본값 반환)
     */
    public static SellerSortBy fromString(String value) {
        if (value == null || value.isBlank()) {
            return defaultSortBy();
        }
        try {
            String normalized = value.toUpperCase().trim().replace("-", "_");
            return SellerSortBy.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            return defaultSortBy();
        }
    }
}
