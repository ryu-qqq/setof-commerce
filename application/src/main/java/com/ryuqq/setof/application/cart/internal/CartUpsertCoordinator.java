package com.ryuqq.setof.application.cart.internal;

import com.ryuqq.setof.application.cart.manager.CartCommandManager;
import com.ryuqq.setof.application.cart.manager.CartReadManager;
import com.ryuqq.setof.domain.cart.Cart;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import com.ryuqq.setof.domain.cart.vo.CartItemDiff;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * CartUpsertCoordinator - 장바구니 항목 Upsert Coordinator.
 *
 * <p>ReadManager로 기존 아이템을 조회하고, Cart.diff()로 Diff를 생성한 뒤 CommandManager로 일괄 영속화합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class CartUpsertCoordinator {

    private final CartReadManager readManager;
    private final CartCommandManager commandManager;

    public CartUpsertCoordinator(CartReadManager readManager, CartCommandManager commandManager) {
        this.readManager = readManager;
        this.commandManager = commandManager;
    }

    public List<CartItem> upsert(Cart cart) {
        List<CartItem> existingItems =
                readManager.findExistingByProductIds(cart.productIds(), cart.userId());
        CartItemDiff diff = cart.diff(existingItems);
        return commandManager.persistAll(diff.allItems());
    }
}
