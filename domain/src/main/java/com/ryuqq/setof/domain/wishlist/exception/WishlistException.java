package com.ryuqq.setof.domain.wishlist.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/**
 * 위시리스트 도메인 예외 기본 클래스.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class WishlistException extends DomainException {

    public WishlistException(WishlistErrorCode errorCode) {
        super(errorCode);
    }

    public WishlistException(WishlistErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public WishlistException(WishlistErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
