package com.ryuqq.setof.application.cart.port.out.command;

import com.ryuqq.setof.domain.cart.aggregate.Cart;
import com.ryuqq.setof.domain.cart.vo.CartId;

/**
 * Cart Persistence Port (Command)
 *
 * <p>Cart Aggregate를 영속화하는 쓰기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CartPersistencePort {

    /**
     * Cart 저장 (신규 생성 또는 수정)
     *
     * <p>Cart 및 CartItem의 상태 변경(소프트 딜리트 포함)은 persist를 통해 처리됩니다.
     *
     * @param cart 저장할 Cart (Domain Aggregate)
     * @return 저장된 Cart의 ID
     */
    CartId persist(Cart cart);
}
