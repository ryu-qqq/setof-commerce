package com.ryuqq.setof.application.cart.service.command;

import com.ryuqq.setof.application.cart.assembler.CartAssembler;
import com.ryuqq.setof.application.cart.dto.command.ClearCartCommand;
import com.ryuqq.setof.application.cart.dto.response.CartResponse;
import com.ryuqq.setof.application.cart.manager.command.CartPersistenceManager;
import com.ryuqq.setof.application.cart.manager.query.CartReadManager;
import com.ryuqq.setof.application.cart.port.in.command.ClearCartUseCase;
import com.ryuqq.setof.domain.cart.aggregate.Cart;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * 장바구니 비우기 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class ClearCartService implements ClearCartUseCase {

    private final CartReadManager cartReadManager;
    private final CartPersistenceManager cartPersistenceManager;
    private final CartAssembler cartAssembler;
    private final ClockHolder clockHolder;

    public ClearCartService(
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
    public CartResponse clear(ClearCartCommand command) {
        Cart cart = cartReadManager.findOrCreateByMemberId(command.memberId());

        Cart clearedCart = cart.clear(Instant.now(clockHolder.getClock()));
        Cart savedCart = cartPersistenceManager.persist(clearedCart);

        return cartAssembler.toResponse(savedCart);
    }
}
