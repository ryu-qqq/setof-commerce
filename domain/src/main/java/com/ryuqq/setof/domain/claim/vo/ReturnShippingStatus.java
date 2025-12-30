package com.ryuqq.setof.domain.claim.vo;

/**
 * ReturnShippingStatus - 반품 배송 상태
 *
 * <p>반품 상품의 수거/배송 진행 상태를 추적합니다.
 *
 * <p>상태 흐름:
 *
 * <pre>
 * PENDING → PICKUP_SCHEDULED → PICKED_UP → IN_TRANSIT → RECEIVED
 *                                    │
 *                                    └→ (직접 발송 시) IN_TRANSIT
 * </pre>
 *
 * @author development-team
 * @since 2.0.0
 */
public enum ReturnShippingStatus {

    /**
     * 대기 중
     *
     * <p>반품 신청 후 배송 정보가 아직 등록되지 않은 상태입니다.
     */
    PENDING("대기중"),

    /**
     * 수거 예약됨
     *
     * <p>방문수거가 예약된 상태입니다. 택배사에 수거 요청이 완료되었습니다.
     */
    PICKUP_SCHEDULED("수거예약"),

    /**
     * 수거 완료
     *
     * <p>택배사가 고객으로부터 상품을 수거한 상태입니다.
     */
    PICKED_UP("수거완료"),

    /**
     * 배송 중
     *
     * <p>반품 상품이 판매자에게 배송 중인 상태입니다.
     */
    IN_TRANSIT("배송중"),

    /**
     * 수령 완료
     *
     * <p>판매자가 반품 상품을 수령하고 검수를 진행할 수 있는 상태입니다.
     */
    RECEIVED("수령완료");

    private final String description;

    ReturnShippingStatus(String description) {
        this.description = description;
    }

    /**
     * 상태 설명 반환
     *
     * @return 상태 설명 (한글)
     */
    public String description() {
        return description;
    }

    /**
     * 기본 상태 반환
     *
     * @return PENDING
     */
    public static ReturnShippingStatus defaultStatus() {
        return PENDING;
    }

    /**
     * 수거 예약 가능 여부
     *
     * @return PENDING 상태면 true
     */
    public boolean canSchedulePickup() {
        return this == PENDING;
    }

    /**
     * 송장 등록 가능 여부
     *
     * @return PENDING 또는 PICKUP_SCHEDULED 상태면 true
     */
    public boolean canRegisterTracking() {
        return this == PENDING || this == PICKUP_SCHEDULED;
    }

    /**
     * 수거 완료 처리 가능 여부
     *
     * @return PICKUP_SCHEDULED 상태면 true
     */
    public boolean canConfirmPickup() {
        return this == PICKUP_SCHEDULED;
    }

    /**
     * 배송 중 전환 가능 여부
     *
     * @return PICKED_UP 또는 PENDING(고객 직접 발송) 상태면 true
     */
    public boolean canTransitToInTransit() {
        return this == PICKED_UP || this == PENDING;
    }

    /**
     * 수령 확인 가능 여부
     *
     * @return IN_TRANSIT 상태면 true
     */
    public boolean canConfirmReceived() {
        return this == IN_TRANSIT;
    }

    /**
     * 취소 가능 여부
     *
     * @return PENDING 또는 PICKUP_SCHEDULED 상태면 true
     */
    public boolean canCancel() {
        return this == PENDING || this == PICKUP_SCHEDULED;
    }

    /**
     * 배송이 진행 중인지 확인
     *
     * @return PICKUP_SCHEDULED, PICKED_UP, IN_TRANSIT 중 하나면 true
     */
    public boolean isInProgress() {
        return this == PICKUP_SCHEDULED || this == PICKED_UP || this == IN_TRANSIT;
    }

    /**
     * 배송이 완료되었는지 확인
     *
     * @return RECEIVED 상태면 true
     */
    public boolean isCompleted() {
        return this == RECEIVED;
    }
}
