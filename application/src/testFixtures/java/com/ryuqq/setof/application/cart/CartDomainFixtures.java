package com.ryuqq.setof.application.cart;

import com.ryuqq.setof.domain.cart.Cart;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import com.ryuqq.setof.domain.cart.id.CartItemId;
import com.ryuqq.setof.domain.cart.vo.CartItemDiff;
import com.ryuqq.setof.domain.cart.vo.CartQuantity;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.LegacyUserId;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.product.id.ProductId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.time.Instant;
import java.util.List;

/**
 * Cart 도메인 객체 테스트 Fixtures.
 *
 * <p>Application 레이어 테스트에서 사용하는 Cart 도메인 객체 생성 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class CartDomainFixtures {

    private static final Instant FIXED_NOW = Instant.parse("2024-01-01T00:00:00Z");
    private static final String MEMBER_ID = "01900000-0000-7000-8000-000000000001";
    private static final Long USER_ID = 100L;

    private CartDomainFixtures() {}

    // ===== CartItem (신규) =====

    public static CartItem newCartItem() {
        return CartItem.forNew(
                MemberId.of(MEMBER_ID),
                LegacyUserId.of(USER_ID),
                ProductGroupId.of(10L),
                ProductId.of(20L),
                CartQuantity.of(2),
                FIXED_NOW);
    }

    public static CartItem newCartItem(Long productGroupId, Long productId, int quantity) {
        return CartItem.forNew(
                MemberId.of(MEMBER_ID),
                LegacyUserId.of(USER_ID),
                ProductGroupId.of(productGroupId),
                ProductId.of(productId),
                CartQuantity.of(quantity),
                FIXED_NOW);
    }

    // ===== CartItem (영속 복원) =====

    public static CartItem persistedCartItem() {
        return CartItem.reconstitute(
                CartItemId.of(1L),
                MemberId.of(MEMBER_ID),
                LegacyUserId.of(USER_ID),
                ProductGroupId.of(10L),
                ProductId.of(20L),
                CartQuantity.of(2),
                DeletionStatus.active(),
                FIXED_NOW,
                FIXED_NOW);
    }

    public static CartItem persistedCartItem(Long cartItemId) {
        return CartItem.reconstitute(
                CartItemId.of(cartItemId),
                MemberId.of(MEMBER_ID),
                LegacyUserId.of(USER_ID),
                ProductGroupId.of(10L),
                ProductId.of(20L + cartItemId),
                CartQuantity.of(2),
                DeletionStatus.active(),
                FIXED_NOW,
                FIXED_NOW);
    }

    public static CartItem persistedCartItem(Long cartItemId, Long productId) {
        return CartItem.reconstitute(
                CartItemId.of(cartItemId),
                MemberId.of(MEMBER_ID),
                LegacyUserId.of(USER_ID),
                ProductGroupId.of(10L),
                ProductId.of(productId),
                CartQuantity.of(2),
                DeletionStatus.active(),
                FIXED_NOW,
                FIXED_NOW);
    }

    public static List<CartItem> persistedCartItems() {
        return List.of(persistedCartItem(1L), persistedCartItem(2L));
    }

    public static List<CartItem> persistedCartItems(List<Long> cartItemIds) {
        return cartItemIds.stream().map(CartDomainFixtures::persistedCartItem).toList();
    }

    // ===== Cart =====

    public static Cart cart() {
        return Cart.of(MEMBER_ID, USER_ID, FIXED_NOW, List.of(newCartItem()));
    }

    public static Cart cart(List<CartItem> items) {
        return Cart.of(MEMBER_ID, USER_ID, FIXED_NOW, items);
    }

    // ===== CartItemDiff =====

    public static CartItemDiff diffWithAddedOnly() {
        return CartItemDiff.of(List.of(newCartItem()), List.of(), FIXED_NOW);
    }

    public static CartItemDiff diffWithUpdatedOnly() {
        return CartItemDiff.of(List.of(), List.of(persistedCartItem()), FIXED_NOW);
    }

    public static CartItemDiff diffWithMixed() {
        return CartItemDiff.of(
                List.of(newCartItem(11L, 21L, 1)), List.of(persistedCartItem()), FIXED_NOW);
    }

    public static CartItemDiff emptyDiff() {
        return CartItemDiff.of(List.of(), List.of(), FIXED_NOW);
    }

    public static Instant fixedNow() {
        return FIXED_NOW;
    }
}
