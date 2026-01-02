package com.ryuqq.setof.application.cart.manager.command;

import com.ryuqq.setof.application.cart.port.out.command.CartPersistencePort;
import com.ryuqq.setof.application.cart.port.out.query.CartQueryPort;
import com.ryuqq.setof.domain.cart.aggregate.Cart;
import com.ryuqq.setof.domain.cart.vo.CartId;
import org.springframework.stereotype.Component;

/**
 * Cart Persistence Manager
 *
 * <p>Cart 영속화 관련 작업을 관리하는 Manager
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CartPersistenceManager {

    private final CartPersistencePort cartPersistencePort;
    private final CartQueryPort cartQueryPort;

    public CartPersistenceManager(
            CartPersistencePort cartPersistencePort, CartQueryPort cartQueryPort) {
        this.cartPersistencePort = cartPersistencePort;
        this.cartQueryPort = cartQueryPort;
    }

    /**
     * Cart 저장
     *
     * <p>저장 후 DB에서 다시 조회하여 CartItem ID가 할당된 Cart를 반환합니다.
     *
     * <p>CartItem의 소프트 딜리트/복원도 persist를 통해 처리됩니다.
     *
     * @param cart 저장할 Cart
     * @return 저장된 Cart (CartItem ID 포함)
     */
    public Cart persist(Cart cart) {
        CartId savedId = cartPersistencePort.persist(cart);
        return cartQueryPort.getById(savedId);
    }
}
