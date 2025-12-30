package com.ryuqq.setof.domain.order.vo;

/**
 * OrderItemStatus - 주문 상품 상태
 *
 * <p>개별 주문 상품의 생명주기를 나타내는 상태값입니다.
 *
 * <p>상태 흐름:
 *
 * <pre>
 * ORDERED → CONFIRMED → SHIPPED → DELIVERED → COMPLETED
 *    │          │
 *    └→ CANCELLED └→ CANCELLED
 * </pre>
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java enum 사용
 * </ul>
 */
public enum OrderItemStatus {

    /** 주문됨 */
    ORDERED("주문됨"),

    /** 주문 확정 */
    CONFIRMED("주문 확정"),

    /** 배송 중 */
    SHIPPED("배송 중"),

    /** 배송 완료 */
    DELIVERED("배송 완료"),

    /** 완료 (구매 확정) */
    COMPLETED("완료"),

    /** 취소됨 */
    CANCELLED("취소됨");

    private final String description;

    OrderItemStatus(String description) {
        this.description = description;
    }

    /**
     * 상태 설명 반환
     *
     * @return 상태 설명
     */
    public String description() {
        return description;
    }

    /**
     * 기본 상태 반환
     *
     * @return ORDERED 상태
     */
    public static OrderItemStatus defaultStatus() {
        return ORDERED;
    }

    /**
     * 최종 상태 여부
     *
     * @return COMPLETED 또는 CANCELLED이면 true
     */
    public boolean isFinal() {
        return this == COMPLETED || this == CANCELLED;
    }
}
