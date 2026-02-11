package com.ryuqq.setof.storage.legacy.order.entity;

/**
 * LegacyOrderStatus - 레거시 주문 상태 Enum.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum LegacyOrderStatus {
    ORDER_FAILED("주문 실패"),
    ORDER_PROCESSING("주문 진행"),
    ORDER_COMPLETED("주문 완료"),

    // 배송
    DELIVERY_PENDING("배송 준비중"),
    DELIVERY_PROCESSING("배송중"),
    DELIVERY_COMPLETED("배송 완료"),

    // 취소
    CANCEL_REQUEST("취소 요청"),
    CANCEL_REQUEST_RECANT("취소 요청 철회"),
    CANCEL_REQUEST_REJECTED("주문 취소 반려"),
    CANCEL_REQUEST_CONFIRMED("취소 요청 승인"),
    SALE_CANCELLED("판매 취소"),

    // 반품
    RETURN_REQUEST("반품 요청"),
    RETURN_DELIVERY_PROCESSING("반품 배송 진행중"),
    RETURN_REQUEST_CONFIRMED("반품 요청 승인"),
    RETURN_REQUEST_RECANT("반품 요청 철회"),
    RETURN_REQUEST_REJECTED("반품 요청 반려"),

    // 정산
    CANCEL_REQUEST_COMPLETED("취소 완료"),
    SALE_CANCELLED_COMPLETED("판매 취소 완료"),
    RETURN_REQUEST_COMPLETED("반품 완료"),
    SETTLEMENT_PROCESSING("정산 예정"),
    SETTLEMENT_COMPLETED("정산 완료");

    private final String displayName;

    LegacyOrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
