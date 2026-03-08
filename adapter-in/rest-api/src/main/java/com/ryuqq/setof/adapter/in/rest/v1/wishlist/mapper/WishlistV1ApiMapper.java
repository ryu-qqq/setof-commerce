package com.ryuqq.setof.adapter.in.rest.v1.wishlist.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.wishlist.dto.request.AddWishlistItemV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.wishlist.dto.response.WishlistItemSliceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.wishlist.dto.response.WishlistItemV1ApiResponse;
import com.ryuqq.setof.application.wishlist.dto.command.AddWishlistItemCommand;
import com.ryuqq.setof.application.wishlist.dto.command.DeleteWishlistItemCommand;
import com.ryuqq.setof.application.wishlist.dto.query.WishlistSearchParams;
import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemResult;
import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemSliceResult;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * WishlistV1ApiMapper - 찜 V1 Public API 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class WishlistV1ApiMapper {

    private static final DateTimeFormatter ISO_8601 =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public AddWishlistItemCommand toAddCommand(Long userId, AddWishlistItemV1ApiRequest request) {
        return new AddWishlistItemCommand(userId, request.productGroupId());
    }

    public DeleteWishlistItemCommand toDeleteCommand(Long userId, long productGroupId) {
        return new DeleteWishlistItemCommand(userId, productGroupId);
    }

    public WishlistSearchParams toSearchParams(Long userId, Long lastDomainId, int size) {
        return WishlistSearchParams.of(userId, lastDomainId, size);
    }

    public WishlistItemSliceV1ApiResponse toSliceResponse(
            WishlistItemSliceResult result, int requestedSize) {
        List<WishlistItemV1ApiResponse> content =
                result.content().stream().map(this::toItemResponse).toList();

        boolean hasNext = result.hasNext();
        boolean last = !hasNext;
        boolean first = true;
        boolean empty = content.isEmpty();
        int numberOfElements = content.size();

        Long lastDomainId = result.sliceMeta().cursorAsLong();
        Long cursorValue = lastDomainId;

        return new WishlistItemSliceV1ApiResponse(
                content,
                last,
                first,
                0,
                WishlistItemSliceV1ApiResponse.unsortedSort(),
                requestedSize,
                numberOfElements,
                empty,
                lastDomainId,
                cursorValue,
                result.totalElements());
    }

    public WishlistItemV1ApiResponse toItemResponse(WishlistItemResult result) {
        WishlistItemV1ApiResponse.BrandResponse brand =
                new WishlistItemV1ApiResponse.BrandResponse(result.brandId(), result.brandName());

        WishlistItemV1ApiResponse.PriceResponse price =
                new WishlistItemV1ApiResponse.PriceResponse(
                        result.regularPrice(), result.currentPrice(), result.discountRate());

        WishlistItemV1ApiResponse.ProductStatusResponse productStatus =
                new WishlistItemV1ApiResponse.ProductStatusResponse(
                        result.isSoldOut(), result.isDisplayed());

        String insertDate =
                result.insertDate() != null ? result.insertDate().format(ISO_8601) : null;

        return new WishlistItemV1ApiResponse(
                result.userFavoriteId(),
                result.productGroupId(),
                result.sellerId(),
                result.productGroupName(),
                brand,
                result.productImageUrl(),
                price,
                insertDate,
                productStatus);
    }
}
