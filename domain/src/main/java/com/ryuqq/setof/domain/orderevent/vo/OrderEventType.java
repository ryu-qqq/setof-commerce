package com.ryuqq.setof.domain.orderevent.vo;

/**
 * OrderEventType - 주문 이벤트 타입
 *
 * <p>주문 타임라인에 기록되는 모든 이벤트 유형을 정의합니다.
 *
 * <p>이벤트 분류:
 *
 * <ul>
 *   <li>ORDER_* : 주문 관련 이벤트
 *   <li>CLAIM_* : 클레임 관련 이벤트
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
public enum OrderEventType {

    // ========== 주문 이벤트 ==========

    /** 주문 생성 */
    ORDER_CREATED("주문이 생성되었습니다"),

    /** 주문 확정 */
    ORDER_CONFIRMED("주문이 확정되었습니다"),

    /** 상품 준비 시작 */
    ORDER_PREPARING("상품 준비가 시작되었습니다"),

    /** 배송 시작 */
    ORDER_SHIPPED("배송이 시작되었습니다"),

    /** 배송 완료 */
    ORDER_DELIVERED("배송이 완료되었습니다"),

    /** 구매 확정 */
    ORDER_COMPLETED("구매가 확정되었습니다"),

    /** 주문 취소 */
    ORDER_CANCELLED("주문이 취소되었습니다"),

    // ========== 클레임 이벤트 ==========

    /** 클레임 요청 */
    CLAIM_REQUESTED("클레임이 요청되었습니다"),

    /** 클레임 승인 */
    CLAIM_APPROVED("클레임이 승인되었습니다"),

    /** 클레임 반려 */
    CLAIM_REJECTED("클레임이 반려되었습니다"),

    /** 클레임 처리 진행 중 */
    CLAIM_IN_PROGRESS("클레임 처리가 진행 중입니다"),

    /** 클레임 완료 */
    CLAIM_COMPLETED("클레임 처리가 완료되었습니다"),

    /** 환불 완료 */
    REFUND_COMPLETED("환불이 완료되었습니다"),

    /** 교환품 발송 */
    EXCHANGE_SHIPPED("교환 상품이 발송되었습니다"),

    /** 반품 수거 완료 */
    RETURN_COLLECTED("반품 수거가 완료되었습니다");

    private final String defaultDescription;

    OrderEventType(String defaultDescription) {
        this.defaultDescription = defaultDescription;
    }

    /**
     * 기본 설명 반환
     *
     * @return 이벤트 기본 설명
     */
    public String defaultDescription() {
        return defaultDescription;
    }

    /**
     * 주문 관련 이벤트인지 확인
     *
     * @return ORDER_로 시작하면 true
     */
    public boolean isOrderEvent() {
        return name().startsWith("ORDER_");
    }

    /**
     * 클레임 관련 이벤트인지 확인
     *
     * @return CLAIM_, REFUND_, EXCHANGE_, RETURN_으로 시작하면 true
     */
    public boolean isClaimEvent() {
        return name().startsWith("CLAIM_")
                || name().startsWith("REFUND_")
                || name().startsWith("EXCHANGE_")
                || name().startsWith("RETURN_");
    }
}
