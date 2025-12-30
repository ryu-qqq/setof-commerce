package com.ryuqq.setof.application.cart.service.query;

import com.ryuqq.setof.application.cart.assembler.CartAssembler;
import com.ryuqq.setof.application.cart.dto.response.CartResponse;
import com.ryuqq.setof.application.cart.manager.query.CartReadManager;
import com.ryuqq.setof.application.cart.port.in.query.GetCartUseCase;
import com.ryuqq.setof.domain.cart.aggregate.Cart;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * 장바구니 조회 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class GetCartService implements GetCartUseCase {

    private final CartReadManager cartReadManager;
    private final CartAssembler cartAssembler;

    public GetCartService(CartReadManager cartReadManager, CartAssembler cartAssembler) {
        this.cartReadManager = cartReadManager;
        this.cartAssembler = cartAssembler;
    }

    @Override
    public CartResponse getCart(UUID memberId) {
        Cart cart = cartReadManager.findOrCreateByMemberId(memberId);
        return cartAssembler.toResponse(cart);
    }

    @Override
    public int getItemCount(UUID memberId) {
        return cartReadManager.findByMemberId(memberId).map(Cart::itemCount).orElse(0);
    }
}
