package com.ryuqq.setof.application.wishlist;

import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemResult;
import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemSliceResult;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.common.vo.SliceMeta;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.wishlist.query.WishlistSearchCriteria;
import com.ryuqq.setof.domain.wishlist.query.WishlistSortKey;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Wishlist Application Query 테스트 Fixtures.
 *
 * <p>찜 항목 조회 관련 SearchCriteria, Result 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class WishlistQueryFixtures {

    private WishlistQueryFixtures() {}

    public static final Long DEFAULT_USER_ID = 1L;
    public static final long DEFAULT_WISHLIST_ITEM_ID = 10L;
    public static final long DEFAULT_PRODUCT_GROUP_ID = 100L;
    public static final int DEFAULT_SIZE = 20;

    // ===== WishlistSearchCriteria =====

    public static WishlistSearchCriteria searchCriteria() {
        return WishlistSearchCriteria.of(
                LegacyMemberId.of(DEFAULT_USER_ID),
                CursorQueryContext.defaultOf(WishlistSortKey.defaultKey()));
    }

    public static WishlistSearchCriteria searchCriteria(Long userId) {
        return WishlistSearchCriteria.of(
                LegacyMemberId.of(userId),
                CursorQueryContext.defaultOf(WishlistSortKey.defaultKey()));
    }

    public static WishlistSearchCriteria searchCriteria(Long userId, int size) {
        return WishlistSearchCriteria.of(
                LegacyMemberId.of(userId),
                CursorQueryContext.firstPage(WishlistSortKey.defaultKey(), null, size));
    }

    // ===== WishlistItemResult =====

    public static WishlistItemResult wishlistItemResult(long wishlistItemId) {
        return WishlistItemResult.of(
                wishlistItemId,
                DEFAULT_PRODUCT_GROUP_ID,
                1L,
                "테스트 상품명",
                10L,
                "테스트 브랜드",
                "https://example.com/image.jpg",
                new BigDecimal("50000"),
                new BigDecimal("40000"),
                20,
                "N",
                "Y",
                LocalDateTime.of(2024, 1, 1, 0, 0, 0));
    }

    public static WishlistItemResult wishlistItemResult(
            long wishlistItemId, long productGroupId, String productGroupName) {
        return WishlistItemResult.of(
                wishlistItemId,
                productGroupId,
                1L,
                productGroupName,
                10L,
                "테스트 브랜드",
                "https://example.com/image.jpg",
                new BigDecimal("50000"),
                new BigDecimal("40000"),
                20,
                "N",
                "Y",
                LocalDateTime.of(2024, 1, 1, 0, 0, 0));
    }

    public static WishlistItemResult soldOutWishlistItemResult(long wishlistItemId) {
        return WishlistItemResult.of(
                wishlistItemId,
                DEFAULT_PRODUCT_GROUP_ID,
                1L,
                "품절 상품",
                10L,
                "테스트 브랜드",
                "https://example.com/image.jpg",
                new BigDecimal("50000"),
                new BigDecimal("50000"),
                0,
                "Y",
                "Y",
                LocalDateTime.of(2024, 1, 1, 0, 0, 0));
    }

    public static List<WishlistItemResult> wishlistItemResults(int count) {
        return java.util.stream.LongStream.rangeClosed(1, count)
                .mapToObj(WishlistQueryFixtures::wishlistItemResult)
                .toList();
    }

    // ===== WishlistItemSliceResult =====

    public static WishlistItemSliceResult sliceResult() {
        List<WishlistItemResult> items = wishlistItemResults(DEFAULT_SIZE);
        SliceMeta sliceMeta = SliceMeta.withCursor((Long) null, DEFAULT_SIZE, false, items.size());
        return WishlistItemSliceResult.of(items, sliceMeta, DEFAULT_SIZE);
    }

    public static WishlistItemSliceResult sliceResultWithNext(int size) {
        List<WishlistItemResult> items = wishlistItemResults(size);
        Long lastId = items.isEmpty() ? null : items.get(items.size() - 1).userFavoriteId();
        SliceMeta sliceMeta = SliceMeta.withCursor(lastId, size, true, items.size());
        return WishlistItemSliceResult.of(items, sliceMeta, 100L);
    }

    public static WishlistItemSliceResult emptySliceResult() {
        return WishlistItemSliceResult.empty();
    }
}
