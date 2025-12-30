package com.ryuqq.setof.domain.cart.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * CartErrorCode - 장바구니 도메인 에러 코드
 *
 * <p>Cart Bounded Context의 모든 에러 코드를 정의합니다.
 *
 * <p>에러 코드 형식: CART-{NUMBER}
 */
public enum CartErrorCode implements ErrorCode {

    // 400 Bad Request
    INVALID_CART_ID("CART-001", 400, "유효하지 않은 장바구니 ID입니다"),
    INVALID_CART_ITEM_ID("CART-002", 400, "유효하지 않은 장바구니 아이템 ID입니다"),
    INVALID_CART_ITEM("CART-003", 400, "유효하지 않은 장바구니 아이템입니다"),
    INVALID_QUANTITY("CART-004", 400, "유효하지 않은 수량입니다"),

    // 404 Not Found
    CART_NOT_FOUND("CART-010", 404, "장바구니를 찾을 수 없습니다"),
    CART_ITEM_NOT_FOUND("CART-011", 404, "장바구니 아이템을 찾을 수 없습니다"),

    // 409 Conflict
    CART_ITEM_LIMIT_EXCEEDED("CART-020", 409, "장바구니 최대 개수를 초과했습니다"),
    QUANTITY_LIMIT_EXCEEDED("CART-021", 409, "상품 최대 수량을 초과했습니다"),
    INSUFFICIENT_STOCK("CART-022", 409, "재고가 부족합니다"),
    PRODUCT_NOT_AVAILABLE("CART-023", 409, "판매 불가 상품입니다");

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
