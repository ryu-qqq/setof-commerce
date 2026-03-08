package com.ryuqq.setof.application.wishlist;

import com.ryuqq.setof.application.wishlist.dto.command.AddWishlistItemCommand;
import com.ryuqq.setof.application.wishlist.dto.command.DeleteWishlistItemCommand;

/**
 * Wishlist Application Command 테스트 Fixtures.
 *
 * <p>찜 항목 추가/삭제 Command 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class WishlistCommandFixtures {

    private WishlistCommandFixtures() {}

    public static final Long DEFAULT_USER_ID = 1L;
    public static final long DEFAULT_PRODUCT_GROUP_ID = 100L;

    // ===== AddWishlistItemCommand =====

    public static AddWishlistItemCommand addCommand() {
        return new AddWishlistItemCommand(DEFAULT_USER_ID, DEFAULT_PRODUCT_GROUP_ID);
    }

    public static AddWishlistItemCommand addCommand(Long userId, long productGroupId) {
        return new AddWishlistItemCommand(userId, productGroupId);
    }

    public static AddWishlistItemCommand addCommand(Long userId) {
        return new AddWishlistItemCommand(userId, DEFAULT_PRODUCT_GROUP_ID);
    }

    // ===== DeleteWishlistItemCommand =====

    public static DeleteWishlistItemCommand deleteCommand() {
        return new DeleteWishlistItemCommand(DEFAULT_USER_ID, DEFAULT_PRODUCT_GROUP_ID);
    }

    public static DeleteWishlistItemCommand deleteCommand(Long userId, long productGroupId) {
        return new DeleteWishlistItemCommand(userId, productGroupId);
    }

    public static DeleteWishlistItemCommand deleteCommand(Long userId) {
        return new DeleteWishlistItemCommand(userId, DEFAULT_PRODUCT_GROUP_ID);
    }
}
