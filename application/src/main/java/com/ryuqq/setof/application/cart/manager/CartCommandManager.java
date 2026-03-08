package com.ryuqq.setof.application.cart.manager;

import com.ryuqq.setof.application.cart.port.out.command.CartCommandPort;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * CartCommandManager - 장바구니 명령 Manager.
 *
 * <p>CommandPort에 위임만 수행합니다. persist/persistAll만 제공하며, 수정/삭제는 Dirty Checking에 의존합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class CartCommandManager {

    private final CartCommandPort commandPort;

    public CartCommandManager(CartCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    @Transactional
    public CartItem persist(CartItem cartItem) {
        return commandPort.persist(cartItem);
    }

    @Transactional
    public List<CartItem> persistAll(List<CartItem> cartItems) {
        return commandPort.persistAll(cartItems);
    }
}
