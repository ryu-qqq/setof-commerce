package com.ryuqq.setof.application.payment.internal;

import com.ryuqq.setof.application.cart.manager.CartCommandManager;
import com.ryuqq.setof.application.cart.manager.CartReadManager;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import com.ryuqq.setof.domain.cart.vo.CartCheckoutItem;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * CartCheckoutCoordinator - 장바구니 결제 시 카트 상태 변경 코디네이터.
 *
 * <p>카트 조회 → 소프트 삭제(remove) → 상태 업데이트를 조율합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class CartCheckoutCoordinator {

    private final CartReadManager cartReadManager;
    private final CartCommandManager cartCommandManager;

    public CartCheckoutCoordinator(
            CartReadManager cartReadManager, CartCommandManager cartCommandManager) {
        this.cartReadManager = cartReadManager;
        this.cartCommandManager = cartCommandManager;
    }

    /**
     * 장바구니 아이템들을 조회하여 소프트 삭제 처리합니다.
     *
     * @param cartCheckoutItems 카트 체크아웃 항목 목록
     */
    public void checkoutAndRemove(List<CartCheckoutItem> cartCheckoutItems) {
        if (cartCheckoutItems.isEmpty()) {
            return;
        }

        long userId = cartCheckoutItems.getFirst().userId();
        List<Long> cartIds = cartCheckoutItems.stream().map(CartCheckoutItem::cartId).toList();

        List<CartItem> cartItems = cartReadManager.findByCartIdsAndUserId(cartIds, userId);

        Instant now = Instant.now();
        for (CartItem cartItem : cartItems) {
            cartItem.remove(now);
        }

        cartCommandManager.persistAll(cartItems);
    }
}
