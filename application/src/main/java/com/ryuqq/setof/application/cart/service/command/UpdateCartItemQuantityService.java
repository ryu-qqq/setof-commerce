package com.ryuqq.setof.application.cart.service.command;

import com.ryuqq.setof.application.cart.assembler.CartAssembler;
import com.ryuqq.setof.application.cart.dto.command.UpdateCartItemQuantityCommand;
import com.ryuqq.setof.application.cart.dto.response.CartResponse;
import com.ryuqq.setof.application.cart.manager.command.CartPersistenceManager;
import com.ryuqq.setof.application.cart.manager.query.CartReadManager;
import com.ryuqq.setof.application.cart.port.in.command.UpdateCartItemQuantityUseCase;
import com.ryuqq.setof.domain.cart.aggregate.Cart;
import com.ryuqq.setof.domain.cart.vo.CartItemId;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * 장바구니 아이템 수량 변경 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateCartItemQuantityService implements UpdateCartItemQuantityUseCase {

    private final CartReadManager cartReadManager;
    private final CartPersistenceManager cartPersistenceManager;
    private final CartAssembler cartAssembler;
    private final ClockHolder clockHolder;

    public UpdateCartItemQuantityService(
            CartReadManager cartReadManager,
            CartPersistenceManager cartPersistenceManager,
            CartAssembler cartAssembler,
            ClockHolder clockHolder) {
        this.cartReadManager = cartReadManager;
        this.cartPersistenceManager = cartPersistenceManager;
        this.cartAssembler = cartAssembler;
        this.clockHolder = clockHolder;
    }

    @Override
    public CartResponse updateQuantity(UpdateCartItemQuantityCommand command) {
        Cart cart = cartReadManager.findOrCreateByMemberId(command.memberId());
        CartItemId cartItemId = CartItemId.of(command.cartItemId());

        Cart updatedCart =
                cart.updateQuantity(
                        cartItemId, command.quantity(), Instant.now(clockHolder.getClock()));
        Cart savedCart = cartPersistenceManager.persist(updatedCart);

        return cartAssembler.toResponse(savedCart);
    }
}
