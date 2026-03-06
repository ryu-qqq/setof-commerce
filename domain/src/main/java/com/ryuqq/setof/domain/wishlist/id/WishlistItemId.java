package com.ryuqq.setof.domain.wishlist.id;

/**
 * 찜 항목 ID Value Object.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record WishlistItemId(Long value) {

    public static WishlistItemId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("WishlistItemId 값은 null일 수 없습니다");
        }
        return new WishlistItemId(value);
    }

    public static WishlistItemId forNew() {
        return new WishlistItemId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
