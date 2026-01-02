package com.ryuqq.setof.application.cart.service.command;

import com.ryuqq.setof.application.cart.assembler.CartAssembler;
import com.ryuqq.setof.application.cart.dto.command.RemoveCartItemCommand;
import com.ryuqq.setof.application.cart.dto.response.CartResponse;
import com.ryuqq.setof.application.cart.manager.command.CartPersistenceManager;
import com.ryuqq.setof.application.cart.manager.query.CartReadManager;
import com.ryuqq.setof.application.cart.port.in.command.RemoveCartItemUseCase;
import com.ryuqq.setof.domain.cart.aggregate.Cart;
import com.ryuqq.setof.domain.cart.vo.CartItemId;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * 장바구니 아이템 삭제 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class RemoveCartItemService implements RemoveCartItemUseCase {

    private final CartReadManager cartReadManager;
    private final CartPersistenceManager cartPersistenceManager;
    private final CartAssembler cartAssembler;
    private final ClockHolder clockHolder;

    public RemoveCartItemService(
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
    public CartResponse removeItems(RemoveCartItemCommand command) {
        Cart cart = cartReadManager.findOrCreateByMemberId(command.memberId());

        List<CartItemId> cartItemIds = command.cartItemIds().stream().map(CartItemId::of).toList();

        Cart updatedCart = cart.removeItems(cartItemIds, Instant.now(clockHolder.getClock()));
        Cart savedCart = cartPersistenceManager.persist(updatedCart);

        return cartAssembler.toResponse(savedCart);
    }

    @Override
    public CartResponse removeSelectedItems(UUID memberId) {
        Cart cart = cartReadManager.findOrCreateByMemberId(memberId);

        Cart updatedCart = cart.removeSelectedItems(Instant.now(clockHolder.getClock()));
        Cart savedCart = cartPersistenceManager.persist(updatedCart);

        return cartAssembler.toResponse(savedCart);
    }
}
