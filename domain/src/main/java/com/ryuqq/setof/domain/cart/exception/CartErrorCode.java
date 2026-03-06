package com.ryuqq.setof.domain.cart.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * 장바구니 도메인 에러 코드.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum CartErrorCode implements ErrorCode {
    CART_ITEM_NOT_FOUND("CART-001", 404, "장바구니 아이템을 찾을 수 없습니다"),
    CART_ITEM_ALREADY_EXISTS("CART-002", 409, "이미 장바구니에 존재하는 상품입니다"),
    CART_ITEM_LIMIT_EXCEEDED("CART-003", 400, "장바구니 아이템 수가 최대 한도를 초과했습니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    CartErrorCode(String code, int httpStatus, String message) {
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
