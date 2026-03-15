package com.setof.commerce.domain.wishlist;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.CursorPageRequest;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.wishlist.aggregate.WishlistItem;
import com.ryuqq.setof.domain.wishlist.id.WishlistItemId;
import com.ryuqq.setof.domain.wishlist.query.WishlistSearchCriteria;
import com.ryuqq.setof.domain.wishlist.query.WishlistSortKey;

/**
 * Wishlist 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 Wishlist 관련 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class WishlistFixtures {

    private WishlistFixtures() {}

    // ===== ID Fixtures =====

    public static WishlistItemId defaultWishlistItemId() {
        return WishlistItemId.of(1L);
    }

    public static WishlistItemId wishlistItemId(Long value) {
        return WishlistItemId.of(value);
    }

    public static WishlistItemId newWishlistItemId() {
        return WishlistItemId.forNew();
    }

    // ===== LegacyMemberId Fixtures =====

    public static LegacyMemberId defaultLegacyMemberId() {
        return LegacyMemberId.of(1001L);
    }

    public static LegacyMemberId legacyMemberId(long value) {
        return LegacyMemberId.of(value);
    }

    // ===== MemberId Fixtures =====

    public static MemberId defaultMemberId() {
        return MemberId.of(1L);
    }

    public static MemberId memberId(Long value) {
        return MemberId.of(value);
    }

    // ===== ProductGroupId Fixtures =====

    public static ProductGroupId defaultProductGroupId() {
        return ProductGroupId.of(100L);
    }

    public static ProductGroupId productGroupId(Long value) {
        return ProductGroupId.of(value);
    }

    // ===== WishlistItem Aggregate Fixtures =====

    public static WishlistItem newWishlistItem() {
        return WishlistItem.create(defaultLegacyMemberId(), defaultProductGroupId());
    }

    public static WishlistItem newWishlistItem(
            LegacyMemberId legacyMemberId, ProductGroupId productGroupId) {
        return WishlistItem.create(legacyMemberId, productGroupId);
    }

    public static WishlistItem activeWishlistItem() {
        return WishlistItem.reconstitute(
                defaultWishlistItemId(),
                defaultLegacyMemberId(),
                defaultMemberId(),
                defaultProductGroupId(),
                DeletionStatus.active(),
                CommonVoFixtures.yesterday());
    }

    public static WishlistItem activeWishlistItem(Long id) {
        return WishlistItem.reconstitute(
                WishlistItemId.of(id),
                defaultLegacyMemberId(),
                defaultMemberId(),
                defaultProductGroupId(),
                DeletionStatus.active(),
                CommonVoFixtures.yesterday());
    }

    public static WishlistItem activeWishlistItemWithoutMemberId() {
        return WishlistItem.reconstitute(
                defaultWishlistItemId(),
                defaultLegacyMemberId(),
                null,
                defaultProductGroupId(),
                DeletionStatus.active(),
                CommonVoFixtures.yesterday());
    }

    public static WishlistItem deletedWishlistItem() {
        return WishlistItem.reconstitute(
                WishlistItemId.of(99L),
                defaultLegacyMemberId(),
                defaultMemberId(),
                defaultProductGroupId(),
                DeletionStatus.deletedAt(CommonVoFixtures.yesterday()),
                CommonVoFixtures.yesterday());
    }

    // ===== CursorQueryContext Fixtures =====

    public static CursorQueryContext<WishlistSortKey, Long> defaultQueryContext() {
        return CursorQueryContext.defaultOf(WishlistSortKey.defaultKey());
    }

    public static CursorQueryContext<WishlistSortKey, Long> queryContextWithCursor(
            Long cursor, int size) {
        return CursorQueryContext.of(
                WishlistSortKey.CREATED_AT,
                SortDirection.DESC,
                CursorPageRequest.afterId(cursor, size));
    }

    public static CursorQueryContext<WishlistSortKey, Long> queryContextWithSize(int size) {
        return CursorQueryContext.of(
                WishlistSortKey.CREATED_AT, SortDirection.DESC, CursorPageRequest.first(size));
    }

    // ===== WishlistSearchCriteria Fixtures =====

    public static WishlistSearchCriteria defaultSearchCriteria() {
        return WishlistSearchCriteria.of(defaultLegacyMemberId(), defaultQueryContext());
    }

    public static WishlistSearchCriteria searchCriteriaWithNullContext() {
        return WishlistSearchCriteria.of(defaultLegacyMemberId(), null);
    }

    public static WishlistSearchCriteria searchCriteriaWithCursor(Long cursor) {
        return WishlistSearchCriteria.of(
                defaultLegacyMemberId(), queryContextWithCursor(cursor, 20));
    }
}
