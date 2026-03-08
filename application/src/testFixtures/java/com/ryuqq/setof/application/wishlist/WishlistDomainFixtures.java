package com.ryuqq.setof.application.wishlist;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.wishlist.aggregate.WishlistItem;
import com.ryuqq.setof.domain.wishlist.id.WishlistItemId;
import java.time.Instant;

/**
 * WishlistItem 도메인 객체 테스트 Fixtures.
 *
 * <p>Application 레이어 테스트에서 사용하는 도메인 객체 생성 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class WishlistDomainFixtures {

    private static final Instant FIXED_NOW = Instant.parse("2024-01-01T00:00:00Z");

    private WishlistDomainFixtures() {}

    // ===== WishlistItem (재구성) =====

    public static WishlistItem activeWishlistItem(
            Long wishlistItemId, Long userId, long productGroupId) {
        return WishlistItem.reconstitute(
                WishlistItemId.of(wishlistItemId),
                LegacyMemberId.of(userId),
                MemberId.of(String.valueOf(userId)),
                ProductGroupId.of(productGroupId),
                DeletionStatus.active(),
                FIXED_NOW);
    }

    public static WishlistItem activeWishlistItem(Long wishlistItemId, Long userId) {
        return activeWishlistItem(wishlistItemId, userId, 100L);
    }

    public static WishlistItem deletedWishlistItem(
            Long wishlistItemId, Long userId, long productGroupId) {
        return WishlistItem.reconstitute(
                WishlistItemId.of(wishlistItemId),
                LegacyMemberId.of(userId),
                MemberId.of(String.valueOf(userId)),
                ProductGroupId.of(productGroupId),
                DeletionStatus.deletedAt(FIXED_NOW),
                FIXED_NOW);
    }

    public static WishlistItem newWishlistItem(Long userId, long productGroupId) {
        return WishlistItem.create(LegacyMemberId.of(userId), ProductGroupId.of(productGroupId));
    }

    public static WishlistItem newWishlistItem(Long userId) {
        return newWishlistItem(userId, 100L);
    }
}
