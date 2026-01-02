package com.ryuqq.setof.application.cart.service.command;

import com.ryuqq.setof.application.cart.assembler.CartAssembler;
import com.ryuqq.setof.application.cart.dto.command.UpdateCartItemSelectedCommand;
import com.ryuqq.setof.application.cart.dto.response.CartResponse;
import com.ryuqq.setof.application.cart.manager.command.CartPersistenceManager;
import com.ryuqq.setof.application.cart.manager.query.CartReadManager;
import com.ryuqq.setof.application.cart.port.in.command.UpdateCartItemSelectedUseCase;
import com.ryuqq.setof.domain.cart.aggregate.Cart;
import com.ryuqq.setof.domain.cart.vo.CartItemId;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * 장바구니 아이템 선택 상태 변경 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateCartItemSelectedService implements UpdateCartItemSelectedUseCase {

    private final CartReadManager cartReadManager;
    private final CartPersistenceManager cartPersistenceManager;
    private final CartAssembler cartAssembler;
    private final ClockHolder clockHolder;

    public UpdateCartItemSelectedService(
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
    public CartResponse updateSelected(UpdateCartItemSelectedCommand command) {
        Cart cart = cartReadManager.findOrCreateByMemberId(command.memberId());

        Cart updatedCart = cart;
        for (Long itemId : command.cartItemIds()) {
            CartItemId cartItemId = CartItemId.of(itemId);
            updatedCart =
                    updatedCart.updateSelected(
                            cartItemId, command.selected(), Instant.now(clockHolder.getClock()));
        }

        Cart savedCart = cartPersistenceManager.persist(updatedCart);
        return cartAssembler.toResponse(savedCart);
    }

    @Override
    public CartResponse updateAllSelected(UUID memberId, boolean selected) {
        Cart cart = cartReadManager.findOrCreateByMemberId(memberId);

        Cart updatedCart = cart.updateAllSelected(selected, Instant.now(clockHolder.getClock()));
        Cart savedCart = cartPersistenceManager.persist(updatedCart);

        return cartAssembler.toResponse(savedCart);
    }
}
