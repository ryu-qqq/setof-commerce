package com.ryuqq.setof.adapter.in.rest.v1.cart;

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
import com.ryuqq.setof.domain.common.vo.SliceMeta;
import java.util.List;
import java.util.Set;

/**
 * Cart V1 API 테스트 Fixtures.
 *
 * <p>장바구니 관련 API Request/Response 및 Application Result/Command 객체를 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class CartApiFixtures {

    private CartApiFixtures() {}

    // ===== Request Fixtures =====

    public static SearchCartsCursorV1ApiRequest searchRequest() {
        return new SearchCartsCursorV1ApiRequest(null, 20);
    }

    public static SearchCartsCursorV1ApiRequest searchRequestWithCursor(Long lastCartId) {
        return new SearchCartsCursorV1ApiRequest(lastCartId, 10);
    }

    public static SearchCartsCursorV1ApiRequest searchRequestWithNullSize() {
        return new SearchCartsCursorV1ApiRequest(null, null);
    }

    public static AddCartItemV1ApiRequest addRequest() {
        return new AddCartItemV1ApiRequest(101L, 1001L, 2, 10L);
    }

    public static List<AddCartItemV1ApiRequest> addRequestList() {
        return List.of(
                new AddCartItemV1ApiRequest(101L, 1001L, 2, 10L),
                new AddCartItemV1ApiRequest(102L, 1002L, 1, 10L));
    }

    public static ModifyCartItemV1ApiRequest modifyRequest() {
        return new ModifyCartItemV1ApiRequest(3);
    }

    public static DeleteCartItemsV1ApiRequest deleteRequest(long cartId) {
        return new DeleteCartItemsV1ApiRequest(cartId);
    }

    // ===== Application Result Fixtures =====

    public static CartCountResult cartCountResult() {
        return CartCountResult.of(5L);
    }

    public static CartPriceResult cartPriceResult() {
        return CartPriceResult.of(150000, 129000, 129000);
    }

    public static CartOptionResult cartOptionResult() {
        return CartOptionResult.of(1L, 10L, "COLOR", "블랙");
    }

    public static CartItemResult cartItemResult(long cartId) {
        return CartItemResult.of(
                cartId,
                10L,
                "나이키",
                101L,
                "에어맥스 90",
                10L,
                "공식스토어",
                1001L,
                cartPriceResult(),
                2,
                50,
                "블랙 270",
                Set.of(cartOptionResult()),
                "https://cdn.example.com/image.jpg",
                "ON_SALE",
                "스포츠>신발");
    }

    public static CartItemResult cartItemResultWithNullOptions(long cartId) {
        return CartItemResult.of(
                cartId,
                10L,
                "나이키",
                101L,
                "에어맥스 90",
                10L,
                "공식스토어",
                1001L,
                cartPriceResult(),
                2,
                50,
                "블랙 270",
                null,
                "https://cdn.example.com/image.jpg",
                "ON_SALE",
                "스포츠>신발");
    }

    public static CartSliceResult cartSliceResult() {
        List<CartItemResult> items = List.of(cartItemResult(1001L), cartItemResult(1002L));
        SliceMeta sliceMeta = SliceMeta.withCursor(1002L, 20, true, items.size());
        return CartSliceResult.of(items, sliceMeta, 10L);
    }

    public static CartSliceResult cartSliceResultEmpty() {
        return CartSliceResult.empty();
    }

    public static CartSliceResult cartSliceResultLastPage() {
        List<CartItemResult> items = List.of(cartItemResult(1003L));
        SliceMeta sliceMeta = SliceMeta.withCursor(1003L, 20, false, items.size());
        return CartSliceResult.of(items, sliceMeta, 3L);
    }

    // ===== Application SearchParams / Command Fixtures =====

    public static CartSearchParams searchParams(Long userId) {
        return CartSearchParams.of(userId, userId, null, 20);
    }

    public static CartSearchParams searchParamsWithCursor(Long userId, Long lastCartId) {
        return CartSearchParams.of(userId, userId, lastCartId, 10);
    }

    public static CartSearchParams countParams(Long userId) {
        return CartSearchParams.forCount(userId, userId);
    }

    public static AddCartItemCommand addCommand(Long userId) {
        List<AddCartItemCommand.CartItemDetail> items =
                List.of(new AddCartItemCommand.CartItemDetail(101L, 1001L, 2, 10L));
        return AddCartItemCommand.of(userId, userId, items);
    }

    public static AddCartItemCommand addCommandMultiple(Long userId) {
        List<AddCartItemCommand.CartItemDetail> items =
                List.of(
                        new AddCartItemCommand.CartItemDetail(101L, 1001L, 2, 10L),
                        new AddCartItemCommand.CartItemDetail(102L, 1002L, 1, 10L));
        return AddCartItemCommand.of(userId, userId, items);
    }

    public static ModifyCartItemCommand modifyCommand(Long cartId, Long userId) {
        return ModifyCartItemCommand.of(cartId, userId, userId, 3);
    }

    public static DeleteCartItemsCommand deleteCommand(Long cartId, Long userId) {
        return DeleteCartItemsCommand.ofSingle(cartId, userId, userId);
    }

    // ===== API Response Fixtures =====

    public static CartCountV1ApiResponse cartCountResponse() {
        return new CartCountV1ApiResponse(5L);
    }

    public static CartV1ApiResponse cartResponse(long cartId) {
        CartV1ApiResponse.PriceResponse price =
                new CartV1ApiResponse.PriceResponse(150000, 129000, 129000, 0, 0, 0);
        CartV1ApiResponse.MileageResponse mileage = new CartV1ApiResponse.MileageResponse(0.0, 0.0);
        Set<CartV1ApiResponse.CategoryResponse> categories =
                Set.of(new CartV1ApiResponse.CategoryResponse(1L, "운동화"));

        return new CartV1ApiResponse(
                10L,
                "나이키",
                "에어맥스 90",
                10L,
                "공식스토어",
                1001L,
                price,
                2,
                50,
                "블랙 270",
                cartId,
                101L,
                "https://cdn.example.com/image.jpg",
                "ON_SALE",
                categories,
                mileage);
    }

    public static CartSliceV1ApiResponse cartSliceResponse() {
        List<CartV1ApiResponse> content = List.of(cartResponse(1001L), cartResponse(1002L));
        return new CartSliceV1ApiResponse(
                content,
                false,
                true,
                0,
                CartSliceV1ApiResponse.SortResponse.defaultUnsorted(),
                20,
                content.size(),
                false,
                1002L,
                "1002",
                10L);
    }

    public static CartSliceV1ApiResponse cartSliceResponseEmpty() {
        return new CartSliceV1ApiResponse(
                List.of(),
                true,
                true,
                0,
                CartSliceV1ApiResponse.SortResponse.defaultUnsorted(),
                20,
                0,
                true,
                null,
                null,
                0L);
    }
}
