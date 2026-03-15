package com.ryuqq.setof.adapter.in.rest.v1.cart.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.request.AddCartItemV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.request.DeleteCartItemsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.request.ModifyCartItemV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.request.SearchCartsCursorV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartCountV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartSliceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartV1ApiResponse;
import com.ryuqq.setof.application.cart.dto.command.AddCartItemCommand;
import com.ryuqq.setof.application.cart.dto.command.DeleteCartItemsCommand;
import com.ryuqq.setof.application.cart.dto.command.ModifyCartItemCommand;
import com.ryuqq.setof.application.cart.dto.query.CartSearchParams;
import com.ryuqq.setof.application.cart.dto.response.CartCountResult;
import com.ryuqq.setof.application.cart.dto.response.CartItemResult;
import com.ryuqq.setof.application.cart.dto.response.CartOptionResult;
import com.ryuqq.setof.application.cart.dto.response.CartPriceResult;
import com.ryuqq.setof.application.cart.dto.response.CartSliceResult;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * CartV1ApiMapper - 장바구니 V1 Public API 변환 매퍼.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class CartV1ApiMapper {

    private static final int DEFAULT_SIZE = 20;

    // ─────────────────────────────────────────────
    // Query 변환
    // ─────────────────────────────────────────────

    /**
     * SearchCartsCursorV1ApiRequest → CartSearchParams 변환.
     *
     * <p>기본값 처리: size가 null이면 20으로 설정. memberId는 레거시 호환으로 userId 문자열 변환.
     */
    public CartSearchParams toSearchParams(Long userId, SearchCartsCursorV1ApiRequest request) {
        int size = (request.size() != null) ? request.size() : DEFAULT_SIZE;
        return CartSearchParams.of(userId, userId, request.lastDomainId(), size);
    }

    /** 카운트 조회용 CartSearchParams 생성. */
    public CartSearchParams toCountParams(Long userId) {
        return CartSearchParams.forCount(userId, userId);
    }

    /** CartCountResult → CartCountV1ApiResponse 변환. */
    public CartCountV1ApiResponse toCartCountResponse(CartCountResult result) {
        return new CartCountV1ApiResponse(result.cartQuantity());
    }

    /**
     * CartSliceResult → CartSliceV1ApiResponse 변환 (레거시 호환 래퍼).
     *
     * @param result 조회 결과
     * @param requestCursor 요청 시 전달된 커서 (first 판단용, null이면 첫 페이지)
     */
    public CartSliceV1ApiResponse toCartSliceResponse(CartSliceResult result, Long requestCursor) {
        List<CartV1ApiResponse> content =
                result.content().stream().map(this::toCartResponse).toList();

        boolean hasNext = result.sliceMeta().hasNext();
        int size = result.sliceMeta().size();
        boolean isFirst = requestCursor == null;

        Long lastDomainId = hasNext ? result.sliceMeta().cursorAsLong() : null;
        String cursorValue = lastDomainId != null ? lastDomainId.toString() : null;

        return new CartSliceV1ApiResponse(
                content,
                !hasNext,
                isFirst,
                0,
                CartSliceV1ApiResponse.SortResponse.defaultUnsorted(),
                size,
                content.size(),
                content.isEmpty(),
                lastDomainId,
                cursorValue,
                result.totalElements());
    }

    private CartV1ApiResponse toCartResponse(CartItemResult r) {
        CartV1ApiResponse.PriceResponse price = toPriceResponse(r.price());

        CartV1ApiResponse.MileageResponse mileage = new CartV1ApiResponse.MileageResponse(0.0, 0.0);

        Set<CartV1ApiResponse.CategoryResponse> categories =
                r.options() != null
                        ? r.options().stream()
                                .map(this::toCategoryResponse)
                                .collect(Collectors.toSet())
                        : Set.of();

        return new CartV1ApiResponse(
                r.brandId(),
                r.brandName(),
                r.productGroupName(),
                r.sellerId(),
                r.sellerName(),
                r.productId(),
                price,
                r.quantity(),
                r.stockQuantity(),
                r.optionValue(),
                r.cartId(),
                r.productGroupId(),
                r.imageUrl(),
                r.productStatus(),
                categories,
                mileage);
    }

    private CartV1ApiResponse.PriceResponse toPriceResponse(CartPriceResult price) {
        if (price == null) {
            return new CartV1ApiResponse.PriceResponse(0, 0, 0, 0, 0, 0);
        }
        return new CartV1ApiResponse.PriceResponse(
                price.regularPrice(),
                price.currentPrice(),
                price.salePrice(),
                price.directDiscountRate(),
                price.directDiscountPrice(),
                price.discountRate());
    }

    private CartV1ApiResponse.CategoryResponse toCategoryResponse(CartOptionResult option) {
        return new CartV1ApiResponse.CategoryResponse(option.optionGroupId(), option.optionName());
    }

    // ─────────────────────────────────────────────
    // Command 변환
    // ─────────────────────────────────────────────

    /**
     * List<AddCartItemV1ApiRequest> → AddCartItemCommand 변환.
     *
     * <p>레거시 호환: memberId는 userId의 문자열 표현으로 대체합니다.
     */
    public AddCartItemCommand toAddCommand(Long userId, List<AddCartItemV1ApiRequest> requests) {
        List<AddCartItemCommand.CartItemDetail> items =
                requests.stream()
                        .map(
                                r ->
                                        new AddCartItemCommand.CartItemDetail(
                                                r.productGroupId(),
                                                r.productId(),
                                                r.quantity(),
                                                r.sellerId()))
                        .toList();
        return AddCartItemCommand.of(userId, userId, items);
    }

    /**
     * List<CartItem> → List<CartV1ApiResponse> 변환 (레거시 응답 호환).
     *
     * <p>CartItem은 Write 모델이므로 최소 필드만 채웁니다.
     */
    public List<CartV1ApiResponse> toCartResponseList(List<CartItem> cartItems) {
        return cartItems.stream().map(this::toCartResponseFromDomain).toList();
    }

    private CartV1ApiResponse toCartResponseFromDomain(CartItem item) {
        CartV1ApiResponse.PriceResponse price =
                new CartV1ApiResponse.PriceResponse(0, 0, 0, 0, 0, 0);
        CartV1ApiResponse.MileageResponse mileage = new CartV1ApiResponse.MileageResponse(0.0, 0.0);

        return new CartV1ApiResponse(
                0L,
                "",
                "",
                0L,
                "",
                item.productIdValue(),
                price,
                item.quantityValue(),
                0,
                "",
                item.idValue() != null ? item.idValue() : 0L,
                item.productGroupIdValue(),
                "",
                "",
                Set.of(),
                mileage);
    }

    /** ModifyCartItemV1ApiRequest → ModifyCartItemCommand 변환. */
    public ModifyCartItemCommand toModifyCommand(
            Long cartId, Long userId, ModifyCartItemV1ApiRequest request) {
        return ModifyCartItemCommand.of(cartId, userId, userId, request.quantity());
    }

    /** DeleteCartItemsV1ApiRequest → DeleteCartItemsCommand 변환. */
    public DeleteCartItemsCommand toDeleteCommand(
            Long userId, DeleteCartItemsV1ApiRequest request) {
        return DeleteCartItemsCommand.ofSingle(request.cartId(), userId, userId);
    }
}
