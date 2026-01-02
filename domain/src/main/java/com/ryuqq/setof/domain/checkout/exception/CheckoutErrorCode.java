package com.ryuqq.setof.domain.checkout.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * CheckoutErrorCode - 결제 세션 도메인 에러 코드
 *
 * <p>Checkout Bounded Context의 모든 에러 코드를 정의합니다.
 *
 * <p>에러 코드 형식: CHECKOUT-{NUMBER}
 */
public enum CheckoutErrorCode implements ErrorCode {

    // 400 Bad Request
    INVALID_CHECKOUT_ID("CHECKOUT-001", 400, "유효하지 않은 결제 세션 ID입니다"),
    INVALID_CHECKOUT_MONEY("CHECKOUT-002", 400, "유효하지 않은 금액입니다"),
    INVALID_CHECKOUT_ITEM("CHECKOUT-003", 400, "유효하지 않은 결제 항목입니다"),
    INVALID_SHIPPING_ADDRESS("CHECKOUT-004", 400, "유효하지 않은 배송지 정보입니다"),
    EMPTY_CHECKOUT_ITEMS("CHECKOUT-005", 400, "결제 항목이 비어있습니다"),

    // 404 Not Found
    CHECKOUT_NOT_FOUND("CHECKOUT-010", 404, "결제 세션을 찾을 수 없습니다"),

    // 409 Conflict
    CHECKOUT_ALREADY_COMPLETED("CHECKOUT-020", 409, "이미 완료된 결제 세션입니다"),
    CHECKOUT_EXPIRED("CHECKOUT-021", 409, "만료된 결제 세션입니다"),
    CHECKOUT_NOT_PROCESSABLE("CHECKOUT-022", 409, "결제 처리를 시작할 수 없는 상태입니다"),
    CHECKOUT_NOT_COMPLETABLE("CHECKOUT-023", 409, "결제를 완료할 수 없는 상태입니다"),
    DUPLICATE_CHECKOUT_REQUEST("CHECKOUT-024", 409, "이미 처리 중인 결제 세션 요청입니다"),
    INSUFFICIENT_STOCK("CHECKOUT-025", 409, "재고가 부족합니다"),
    LOCK_ACQUISITION_FAILED("CHECKOUT-026", 409, "락 획득에 실패했습니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    CheckoutErrorCode(String code, int httpStatus, String message) {
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
