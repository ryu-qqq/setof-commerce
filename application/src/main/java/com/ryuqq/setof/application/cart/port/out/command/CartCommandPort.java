package com.ryuqq.setof.application.cart.port.out.command;

import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import java.util.List;

/**
 * CartCommandPort - 장바구니 명령 Port.
 *
 * <p>Adapter가 구현할 출력 포트입니다. persist/persistAll만 제공하며, 수정/삭제는 Dirty Checking에 의존합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface CartCommandPort {

    CartItem persist(CartItem cartItem);

    List<CartItem> persistAll(List<CartItem> cartItems);
}
