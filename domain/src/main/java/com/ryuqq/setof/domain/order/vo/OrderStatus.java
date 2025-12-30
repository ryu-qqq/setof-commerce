package com.ryuqq.setof.domain.order.vo;

/**
 * OrderStatus - 주문 상태
 *
 * <p>주문의 생명주기를 나타내는 상태값입니다.
 *
 * <p>상태 흐름:
 *
 * <pre>
 * PENDING → CONFIRMED → PREPARING → SHIPPED → DELIVERED → COMPLETED
 *    │          │
 *    └→ CANCELLED └→ CANCELLED
 * </pre>
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java enum 사용
 *   <li>상태 전이 검증 메서드 제공
 * </ul>
 */
public enum OrderStatus {

    /** 주문 대기 (결제 완료 직후) */
    PENDING("주문 대기"),

    /** 주문 확정 (판매자 확인) */
    CONFIRMED("주문 확정"),

    /** 상품 준비 중 */
    PREPARING("상품 준비 중"),

    /** 배송 중 */
    SHIPPED("배송 중"),

    /** 배송 완료 */
    DELIVERED("배송 완료"),

    /** 주문 완료 (구매 확정) */
    COMPLETED("주문 완료"),

    /** 주문 취소 */
    CANCELLED("주문 취소");

    private final String description;

    OrderStatus(String description) {
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
     * @return PENDING 상태
     */
    public static OrderStatus defaultStatus() {
        return PENDING;
    }

    /**
     * 주문 확정 가능 여부
     *
     * @return PENDING 상태이면 true
     */
    public boolean canConfirm() {
        return this == PENDING;
    }

    /**
     * 상품 준비 시작 가능 여부
     *
     * @return CONFIRMED 상태이면 true
     */
    public boolean canStartPreparing() {
        return this == CONFIRMED;
    }

    /**
     * 배송 시작 가능 여부
     *
     * @return PREPARING 상태이면 true
     */
    public boolean canShip() {
        return this == PREPARING;
    }

    /**
     * 배송 완료 처리 가능 여부
     *
     * @return SHIPPED 상태이면 true
     */
    public boolean canDeliver() {
        return this == SHIPPED;
    }

    /**
     * 구매 확정 가능 여부
     *
     * @return DELIVERED 상태이면 true
     */
    public boolean canComplete() {
        return this == DELIVERED;
    }

    /**
     * 취소 가능 여부
     *
     * @return PENDING, CONFIRMED, PREPARING 상태이면 true
     */
    public boolean canCancel() {
        return this == PENDING || this == CONFIRMED || this == PREPARING;
    }

    /**
     * 최종 상태 여부 (더 이상 상태 변경 불가)
     *
     * @return COMPLETED 또는 CANCELLED이면 true
     */
    public boolean isFinal() {
        return this == COMPLETED || this == CANCELLED;
    }

    /**
     * 배송 전 상태 여부
     *
     * @return 배송 전이면 true
     */
    public boolean isBeforeShipping() {
        return this == PENDING || this == CONFIRMED || this == PREPARING;
    }
}
