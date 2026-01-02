package com.ryuqq.setof.application.cart.service.command;

import com.ryuqq.setof.application.cart.assembler.CartAssembler;
import com.ryuqq.setof.application.cart.dto.command.AddCartItemCommand;
import com.ryuqq.setof.application.cart.dto.response.CartResponse;
import com.ryuqq.setof.application.cart.factory.command.CartCommandFactory;
import com.ryuqq.setof.application.cart.manager.command.CartPersistenceManager;
import com.ryuqq.setof.application.cart.manager.query.CartReadManager;
import com.ryuqq.setof.application.cart.port.in.command.AddCartItemUseCase;
import com.ryuqq.setof.domain.cart.aggregate.Cart;
import com.ryuqq.setof.domain.cart.vo.CartItem;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * 장바구니 아이템 추가 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class AddCartItemService implements AddCartItemUseCase {

    private final CartReadManager cartReadManager;
    private final CartPersistenceManager cartPersistenceManager;
    private final CartCommandFactory cartCommandFactory;
    private final CartAssembler cartAssembler;
    private final ClockHolder clockHolder;

    public AddCartItemService(
            CartReadManager cartReadManager,
            CartPersistenceManager cartPersistenceManager,
            CartCommandFactory cartCommandFactory,
            CartAssembler cartAssembler,
            ClockHolder clockHolder) {
        this.cartReadManager = cartReadManager;
        this.cartPersistenceManager = cartPersistenceManager;
        this.cartCommandFactory = cartCommandFactory;
        this.cartAssembler = cartAssembler;
        this.clockHolder = clockHolder;
    }

    @Override
    public CartResponse addItem(AddCartItemCommand command) {
        Cart cart = cartReadManager.findOrCreateByMemberId(command.memberId());
        CartItem newItem = cartCommandFactory.toCartItem(command);

        Cart updatedCart = cart.addItem(newItem, Instant.now(clockHolder.getClock()));
        Cart savedCart = cartPersistenceManager.persist(updatedCart);

        return cartAssembler.toResponse(savedCart);
    }
}
