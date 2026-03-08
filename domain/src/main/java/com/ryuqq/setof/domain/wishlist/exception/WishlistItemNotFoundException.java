package com.ryuqq.setof.domain.wishlist.exception;

/**
 * 위시리스트 아이템 미발견 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class WishlistItemNotFoundException extends WishlistException {

    private static final WishlistErrorCode ERROR_CODE = WishlistErrorCode.WISHLIST_ITEM_NOT_FOUND;

    public WishlistItemNotFoundException() {
        super(ERROR_CODE);
    }

    public WishlistItemNotFoundException(Long wishlistItemId) {
        super(ERROR_CODE, String.format("ID가 %d인 위시리스트 아이템을 찾을 수 없습니다", wishlistItemId));
    }
}
