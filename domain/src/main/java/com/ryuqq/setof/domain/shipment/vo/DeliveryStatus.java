package com.ryuqq.setof.domain.shipment.vo;

/**
 * 배송 상태 Value Object (Enum)
 *
 * <p>운송장의 배송 진행 상태를 나타냅니다.
 *
 * <p>상태 전이: PENDING → IN_TRANSIT → OUT_FOR_DELIVERY → DELIVERED
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Enum 사용
 * </ul>
 */
public enum DeliveryStatus {

    /** 발송 대기 - 운송장 등록됨, 아직 발송되지 않음 */
    PENDING("발송대기", 10, false),

    /** 배송 중 - 택배사에서 수거 후 이동 중 */
    IN_TRANSIT("배송중", 20, false),

    /** 배송 출발 - 배송 기사 배정, 배송지로 이동 중 */
    OUT_FOR_DELIVERY("배송출발", 30, false),

    /** 배송 완료 - 수령인에게 전달 완료 */
    DELIVERED("배송완료", 40, true);

    private final String description;
    private final int order;
    private final boolean terminal;

    DeliveryStatus(String description, int order, boolean terminal) {
        this.description = description;
        this.order = order;
        this.terminal = terminal;
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
     * 상태 순서 반환 (진행 순서)
     *
     * @return 순서 값
     */
    public int order() {
        return order;
    }

    /**
     * 종료 상태 여부 확인
     *
     * @return 종료 상태이면 true (더 이상 상태 변경 불가)
     */
    public boolean isTerminal() {
        return terminal;
    }

    /**
     * 기본 상태 반환
     *
     * @return PENDING 상태
     */
    public static DeliveryStatus defaultStatus() {
        return PENDING;
    }

    /**
     * 해당 상태로 전이 가능한지 확인 (Tell, Don't Ask)
     *
     * @param target 목표 상태
     * @return 전이 가능하면 true
     */
    public boolean canTransitionTo(DeliveryStatus target) {
        if (this.isTerminal()) {
            return false;
        }
        return target.order > this.order;
    }

    /**
     * 배송 완료 여부 확인
     *
     * @return 배송 완료 상태이면 true
     */
    public boolean isDelivered() {
        return this == DELIVERED;
    }

    /**
     * 배송 진행 중 여부 확인
     *
     * @return 배송 진행 중이면 true (PENDING, IN_TRANSIT, OUT_FOR_DELIVERY)
     */
    public boolean isInProgress() {
        return !this.isTerminal();
    }
}
