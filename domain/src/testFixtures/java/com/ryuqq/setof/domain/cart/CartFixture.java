package com.ryuqq.setof.domain.cart;

import com.ryuqq.setof.domain.cart.aggregate.Cart;
import com.ryuqq.setof.domain.cart.vo.CartId;
import com.ryuqq.setof.domain.cart.vo.CartItem;
import com.ryuqq.setof.domain.cart.vo.CartItemId;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Cart 도메인 테스트 Fixture
 *
 * <p>다른 모듈의 테스트에서 Cart 도메인 객체를 생성할 때 사용합니다.
 */
public final class CartFixture {

    private CartFixture() {
        // Utility class
    }

    private static final Instant NOW = Instant.now();
    private static final UUID DEFAULT_MEMBER_ID =
            UUID.fromString("019538ab-faac-7ab6-9ab0-17e7f91a51c7");

    // ===== Cart Fixtures =====

    /** 신규 빈 장바구니 생성 */
    public static Cart emptyCart(UUID memberId) {
        return Cart.forNew(memberId, NOW);
    }

    /** 아이템이 포함된 장바구니 생성 */
    public static Cart cartWithItems(UUID memberId, List<CartItem> items) {
        Cart cart = Cart.forNew(memberId, NOW);
        for (CartItem item : items) {
            cart = cart.addItem(item, NOW);
        }
        return cart;
    }

    /** 영속화된 장바구니 복원 (ID 포함) */
    public static Cart restoredCart(Long cartId, UUID memberId, List<CartItem> items) {
        return Cart.restore(CartId.of(cartId), memberId, items, NOW.minusSeconds(3600), NOW);
    }

    /** 기본 테스트용 장바구니 (아이템 1개) */
    public static Cart defaultCart() {
        List<CartItem> items = new ArrayList<>();
        items.add(restoredCartItem(1L, 100L, 10L, 1L, 1L, 2, BigDecimal.valueOf(10000)));
        return Cart.restore(CartId.of(1L), DEFAULT_MEMBER_ID, items, NOW.minusSeconds(3600), NOW);
    }

    /** 여러 판매자 상품이 포함된 장바구니 */
    public static Cart cartWithMultipleSellers() {
        List<CartItem> items = new ArrayList<>();
        items.add(restoredCartItem(1L, 100L, 10L, 1L, 1L, 2, BigDecimal.valueOf(10000)));
        items.add(restoredCartItem(2L, 200L, 20L, 2L, 1L, 3, BigDecimal.valueOf(5000)));
        items.add(restoredCartItem(3L, 300L, 30L, 3L, 2L, 1, BigDecimal.valueOf(15000)));
        return Cart.restore(CartId.of(1L), DEFAULT_MEMBER_ID, items, NOW.minusSeconds(3600), NOW);
    }

    // ===== CartItem Fixtures =====

    /** 신규 CartItem 생성 */
    public static CartItem newCartItem(
            Long productStockId,
            Long productId,
            Long productGroupId,
            Long sellerId,
            int quantity,
            BigDecimal unitPrice) {
        return CartItem.forNew(
                productStockId, productId, productGroupId, sellerId, quantity, unitPrice, NOW);
    }

    /** 영속화된 CartItem 복원 */
    public static CartItem restoredCartItem(
            Long cartItemId,
            Long productStockId,
            Long productId,
            Long productGroupId,
            Long sellerId,
            int quantity,
            BigDecimal unitPrice) {
        return CartItem.restore(
                CartItemId.of(cartItemId),
                productStockId,
                productId,
                productGroupId,
                sellerId,
                quantity,
                unitPrice,
                true,
                NOW.minusSeconds(3600),
                null); // deletedAt - 삭제되지 않은 상태
    }

    /** 소프트 딜리트된 CartItem 복원 */
    public static CartItem deletedCartItem(
            Long cartItemId,
            Long productStockId,
            Long productId,
            Long productGroupId,
            Long sellerId,
            int quantity,
            BigDecimal unitPrice) {
        return CartItem.restore(
                CartItemId.of(cartItemId),
                productStockId,
                productId,
                productGroupId,
                sellerId,
                quantity,
                unitPrice,
                true,
                NOW.minusSeconds(3600),
                NOW.minusSeconds(1800)); // deletedAt - 30분 전 삭제됨
    }

    /** 기본 테스트용 CartItem */
    public static CartItem defaultCartItem() {
        return newCartItem(100L, 10L, 1L, 1L, 2, BigDecimal.valueOf(10000));
    }

    /** 최대 수량 CartItem */
    public static CartItem maxQuantityCartItem() {
        return newCartItem(100L, 10L, 1L, 1L, CartItem.MAX_QUANTITY, BigDecimal.valueOf(10000));
    }

    // ===== CartId Fixtures =====

    public static CartId cartId(Long value) {
        return CartId.of(value);
    }

    // ===== CartItemId Fixtures =====

    public static CartItemId cartItemId(Long value) {
        return CartItemId.of(value);
    }
}
