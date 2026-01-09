package com.ryuqq.setof.batch.legacy.order.enums;

import java.util.List;
import java.util.Map;

/**
 * 주문 상태 Enum
 *
 * @author development-team
 * @since 1.0.0
 */
public enum OrderStatus {
    ORDER_PROCESSING,

    SALE_CANCELLED,
    SALE_CANCELLED_COMPLETED,

    DELIVERY_PROCESSING,
    DELIVERY_COMPLETED,

    CANCEL_REQUEST,
    CANCEL_REQUEST_REJECTED,
    CANCEL_REQUEST_CONFIRMED,
    CANCEL_REQUEST_COMPLETED,

    RETURN_REQUEST,
    RETURN_REQUEST_REJECTED,
    RETURN_REQUEST_CONFIRMED,
    RETURN_REQUEST_COMPLETED,

    SETTLEMENT_PROCESSING;

    /** 상태 전이 매핑 */
    public static Map<OrderStatus, OrderStatus> getStatusMapping() {
        return Map.of(
                DELIVERY_COMPLETED, SETTLEMENT_PROCESSING,
                CANCEL_REQUEST_REJECTED, DELIVERY_PROCESSING,
                RETURN_REQUEST_REJECTED, DELIVERY_COMPLETED,
                CANCEL_REQUEST_CONFIRMED, CANCEL_REQUEST_COMPLETED,
                SALE_CANCELLED, SALE_CANCELLED_COMPLETED,
                RETURN_REQUEST_CONFIRMED, RETURN_REQUEST_COMPLETED);
    }

    /** 거부된 주문 상태 목록 */
    public static List<OrderStatus> rejectedStatuses() {
        return List.of(CANCEL_REQUEST_REJECTED, RETURN_REQUEST_REJECTED);
    }

    /** 완료 처리할 주문 상태 목록 */
    public static List<OrderStatus> completedStatuses() {
        return List.of(CANCEL_REQUEST_CONFIRMED, SALE_CANCELLED, RETURN_REQUEST_CONFIRMED);
    }

    /** 정산 처리로 전환할 상태 목록 */
    public static List<OrderStatus> settlementProcessingStatuses() {
        return List.of(DELIVERY_COMPLETED);
    }

    /** 취소 요청 상태 목록 */
    public static List<OrderStatus> cancelRequestStatuses() {
        return List.of(CANCEL_REQUEST);
    }

    /** 결제 실패 처리 대상 상태 (가상계좌 포함) */
    public static List<OrderStatus> paymentFailStatuses() {
        return List.of(ORDER_PROCESSING);
    }
}
