package com.ryuqq.setof.adapter.in.rest.v1.wishlist;

import com.ryuqq.setof.adapter.in.rest.v1.wishlist.dto.request.AddWishlistItemV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.wishlist.dto.response.WishlistItemSliceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.wishlist.dto.response.WishlistItemV1ApiResponse;
import com.ryuqq.setof.application.wishlist.dto.command.AddWishlistItemCommand;
import com.ryuqq.setof.application.wishlist.dto.command.DeleteWishlistItemCommand;
import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemResult;
import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemSliceResult;
import com.ryuqq.setof.domain.common.vo.SliceMeta;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Wishlist V1 API 테스트 Fixtures.
 *
 * <p>찜 관련 API Request/Response 및 Application Result/Command 객체를 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class WishlistApiFixtures {

    private WishlistApiFixtures() {}

    // ===== Request Fixtures =====

    public static AddWishlistItemV1ApiRequest addRequest() {
        return new AddWishlistItemV1ApiRequest(1001L);
    }

    // ===== Application Result Fixtures =====

    public static WishlistItemResult wishlistItemResult(long userFavoriteId) {
        return WishlistItemResult.of(
                userFavoriteId,
                1001L,
                5L,
                "나이키 에어맥스 90",
                3L,
                "Nike",
                "https://example.com/image.jpg",
                new BigDecimal("50000"),
                new BigDecimal("40000"),
                20,
                "N",
                "Y",
                LocalDateTime.of(2024, 1, 1, 10, 30, 0));
    }

    public static WishlistItemResult soldOutWishlistItemResult(long userFavoriteId) {
        return WishlistItemResult.of(
                userFavoriteId,
                2002L,
                6L,
                "아디다스 스탠스미스",
                4L,
                "Adidas",
                "https://example.com/adidas.jpg",
                new BigDecimal("80000"),
                new BigDecimal("80000"),
                0,
                "Y",
                "Y",
                LocalDateTime.of(2024, 2, 1, 9, 0, 0));
    }

    public static WishlistItemSliceResult wishlistItemSliceResult() {
        List<WishlistItemResult> items =
                List.of(wishlistItemResult(1L), soldOutWishlistItemResult(2L));
        SliceMeta sliceMeta = SliceMeta.withCursor(2L, 20, true, items.size());
        return WishlistItemSliceResult.of(items, sliceMeta, 42L);
    }

    public static WishlistItemSliceResult emptySliceResult() {
        return WishlistItemSliceResult.empty();
    }

    // ===== Application Command Fixtures =====

    public static AddWishlistItemCommand addCommand(Long userId) {
        return new AddWishlistItemCommand(userId, 1001L);
    }

    public static DeleteWishlistItemCommand deleteCommand(Long userId, long productGroupId) {
        return new DeleteWishlistItemCommand(userId, productGroupId);
    }

    // ===== API Response Fixtures =====

    public static WishlistItemV1ApiResponse wishlistItemResponse(long userFavoriteId) {
        WishlistItemV1ApiResponse.BrandResponse brand =
                new WishlistItemV1ApiResponse.BrandResponse(3L, "Nike");
        WishlistItemV1ApiResponse.PriceResponse price =
                new WishlistItemV1ApiResponse.PriceResponse(
                        new BigDecimal("50000"), new BigDecimal("40000"), 20);
        WishlistItemV1ApiResponse.ProductStatusResponse productStatus =
                new WishlistItemV1ApiResponse.ProductStatusResponse(false, true);
        return new WishlistItemV1ApiResponse(
                userFavoriteId,
                1001L,
                5L,
                "나이키 에어맥스 90",
                brand,
                "https://example.com/image.jpg",
                price,
                "2024-01-01T10:30:00",
                productStatus);
    }

    public static WishlistItemSliceV1ApiResponse wishlistItemSliceResponse() {
        List<WishlistItemV1ApiResponse> content = List.of(wishlistItemResponse(1L));
        return new WishlistItemSliceV1ApiResponse(
                content,
                false,
                true,
                0,
                WishlistItemSliceV1ApiResponse.unsortedSort(),
                20,
                content.size(),
                content.isEmpty(),
                1L,
                1L,
                42L);
    }

    public static WishlistItemSliceV1ApiResponse emptySliceResponse() {
        return new WishlistItemSliceV1ApiResponse(
                List.of(),
                true,
                true,
                0,
                WishlistItemSliceV1ApiResponse.unsortedSort(),
                20,
                0,
                true,
                null,
                null,
                0L);
    }
}
