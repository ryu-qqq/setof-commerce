package com.ryuqq.setof.domain.order.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * OrderErrorCode - 주문 도메인 에러 코드
 *
 * <p>Order Bounded Context의 모든 에러 코드를 정의합니다.
 *
 * <p>에러 코드 형식: ORDER-{NUMBER}
 */
public enum OrderErrorCode implements ErrorCode {

    // 400 Bad Request
    INVALID_ORDER_ID("ORDER-001", 400, "유효하지 않은 주문 ID입니다"),
    INVALID_ORDER_NUMBER("ORDER-002", 400, "유효하지 않은 주문 번호입니다"),
    INVALID_ORDER_ITEM_ID("ORDER-003", 400, "유효하지 않은 주문 상품 ID입니다"),
    INVALID_ORDER_MONEY("ORDER-004", 400, "유효하지 않은 주문 금액입니다"),
    INVALID_PRODUCT_SNAPSHOT("ORDER-005", 400, "유효하지 않은 상품 스냅샷입니다"),
    INVALID_SHIPPING_INFO("ORDER-006", 400, "유효하지 않은 배송 정보입니다"),
    EMPTY_ORDER_ITEMS("ORDER-007", 400, "주문 상품이 비어있습니다"),
    INVALID_QUANTITY("ORDER-008", 400, "유효하지 않은 수량입니다"),

    // 404 Not Found
    ORDER_NOT_FOUND("ORDER-010", 404, "주문을 찾을 수 없습니다"),
    ORDER_ITEM_NOT_FOUND("ORDER-011", 404, "주문 상품을 찾을 수 없습니다"),

    // 409 Conflict
    ORDER_ALREADY_CONFIRMED("ORDER-020", 409, "이미 확정된 주문입니다"),
    ORDER_ALREADY_CANCELLED("ORDER-021", 409, "이미 취소된 주문입니다"),
    ORDER_ALREADY_COMPLETED("ORDER-022", 409, "이미 완료된 주문입니다"),
    ORDER_NOT_CONFIRMABLE("ORDER-023", 409, "확정할 수 없는 주문 상태입니다"),
    ORDER_NOT_CANCELLABLE("ORDER-024", 409, "취소할 수 없는 주문 상태입니다"),
    ORDER_NOT_SHIPPABLE("ORDER-025", 409, "배송 처리할 수 없는 주문 상태입니다"),
    QUANTITY_EXCEEDS_AVAILABLE("ORDER-026", 409, "수량이 가용 수량을 초과합니다"),

    // === 할인 관련 (ORDER-030 ~ ORDER-039) ===
    INVALID_ORDER_DISCOUNT("ORDER-030", 400, "유효하지 않은 주문 할인입니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    OrderErrorCode(String code, int httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
