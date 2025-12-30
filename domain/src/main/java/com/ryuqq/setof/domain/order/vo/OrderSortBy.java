package com.ryuqq.setof.domain.order.vo;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * OrderSortBy - 주문 정렬 필드 Value Object
 *
 * <p>주문 목록 조회 시 정렬 기준 필드를 지정합니다. 정렬 방향(ASC/DESC)은 별도의 SortDirection을 사용합니다.
 *
 * <p><strong>사용 예시:</strong>
 *
 * <pre>{@code
 * // 주문일순 내림차순 정렬
 * OrderSearchCriteria criteria = OrderSearchCriteria.builder()
 *     .sortBy(OrderSortBy.ORDER_DATE)
 *     .sortDirection(SortDirection.DESC)
 *     .build();
 * }</pre>
 *
 * @author development-team
 * @since 1.0.0
 */
public enum OrderSortBy implements SortKey {

    /** 주문 ID 기준 정렬 */
    ID("id", "ID순"),

    /** 주문일 기준 정렬 */
    ORDER_DATE("orderDate", "주문일순"),

    /** 주문 생성일 기준 정렬 */
    CREATED_AT("createdAt", "등록일순"),

    /** 주문 수정일 기준 정렬 */
    UPDATED_AT("updatedAt", "수정일순"),

    /** 주문 금액 기준 정렬 */
    TOTAL_AMOUNT("totalAmount", "금액순");

    private final String fieldName;
    private final String displayName;

    OrderSortBy(String fieldName, String displayName) {
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
    public static OrderSortBy defaultSortBy() {
        return CREATED_AT;
    }

    /**
     * 문자열로부터 OrderSortBy 파싱 (대소문자 무관)
     *
     * @param value 문자열 (예: "order_date", "ORDER_DATE", "orderDate")
     * @return OrderSortBy (null이거나 유효하지 않으면 기본값 반환)
     */
    public static OrderSortBy fromString(String value) {
        if (value == null || value.isBlank()) {
            return defaultSortBy();
        }
        try {
            String normalized = value.toUpperCase().trim().replace("-", "_");
            return OrderSortBy.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            return defaultSortBy();
        }
    }
}
