package com.ryuqq.setof.domain.wishlist.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * 위시리스트 도메인 에러 코드.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum WishlistErrorCode implements ErrorCode {
    WISHLIST_ITEM_NOT_FOUND("WSH-001", 404, "위시리스트 아이템을 찾을 수 없습니다"),
    WISHLIST_ITEM_ALREADY_EXISTS("WSH-002", 409, "이미 위시리스트에 존재하는 상품입니다"),
    WISHLIST_ITEM_LIMIT_EXCEEDED("WSH-003", 400, "위시리스트 아이템 수가 최대 한도를 초과했습니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    WishlistErrorCode(String code, int httpStatus, String message) {
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
