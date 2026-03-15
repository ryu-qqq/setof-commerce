package com.ryuqq.setof.application.cart;

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
 * Cart Application Query 테스트 Fixtures.
 *
 * <p>장바구니 조회 관련 Query 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class CartQueryFixtures {

    private CartQueryFixtures() {}

    public static final Long MEMBER_ID = 1L;
    public static final Long USER_ID = 100L;

    // ===== CartSearchParams =====

    public static CartSearchParams searchParams() {
        return CartSearchParams.of(MEMBER_ID, USER_ID, null, 20);
    }

    public static CartSearchParams searchParams(Long lastCartId, int size) {
        return CartSearchParams.of(MEMBER_ID, USER_ID, lastCartId, size);
    }

    public static CartSearchParams searchParamsForCount() {
        return CartSearchParams.forCount(MEMBER_ID, USER_ID);
    }

    public static CartSearchParams searchParamsFirstPage(int size) {
        return CartSearchParams.of(MEMBER_ID, USER_ID, null, size);
    }

    // ===== CartItemResult =====

    public static CartItemResult cartItemResult(long cartId) {
        return CartItemResult.of(
                cartId,
                1L,
                "테스트브랜드",
                10L,
                "테스트상품그룹",
                5L,
                "테스트셀러",
                20L,
                CartPriceResult.of(15000, 12000, 12000),
                2,
                10,
                "COLOR:RED / SIZE:XL",
                Set.of(
                        CartOptionResult.of(1L, 10L, "COLOR", "RED"),
                        CartOptionResult.of(2L, 20L, "SIZE", "XL")),
                "http://example.com/image.jpg",
                "ON_SALE",
                "패션 > 의류 > 상의");
    }

    public static List<CartItemResult> cartItemResults() {
        return List.of(cartItemResult(1L), cartItemResult(2L));
    }

    public static List<CartItemResult> cartItemResults(int count) {
        return java.util.stream.LongStream.rangeClosed(1, count)
                .mapToObj(CartQueryFixtures::cartItemResult)
                .toList();
    }

    // ===== CartSliceResult =====

    public static CartSliceResult cartSliceResult() {
        List<CartItemResult> items = cartItemResults();
        SliceMeta sliceMeta = SliceMeta.withCursor((Long) null, 20, false, items.size());
        return CartSliceResult.of(items, sliceMeta, 2L);
    }

    public static CartSliceResult cartSliceResultWithNext() {
        List<CartItemResult> items = cartItemResults();
        SliceMeta sliceMeta = SliceMeta.withCursor(2L, 20, true, items.size());
        return CartSliceResult.of(items, sliceMeta, 10L);
    }

    public static CartSliceResult emptyCartSliceResult() {
        return CartSliceResult.empty();
    }

    // ===== CartCountResult =====

    public static CartCountResult cartCountResult() {
        return CartCountResult.of(5L);
    }

    public static CartCountResult cartCountResult(long count) {
        return CartCountResult.of(count);
    }

    public static CartCountResult emptyCartCountResult() {
        return CartCountResult.of(0L);
    }
}
