package com.ryuqq.setof.application.cart.service.command;

import com.ryuqq.setof.application.cart.dto.command.AddCartItemCommand;
import com.ryuqq.setof.application.cart.factory.CartCommandFactory;
import com.ryuqq.setof.application.cart.internal.CartUpsertCoordinator;
import com.ryuqq.setof.application.cart.port.in.command.AddCartItemUseCase;
import com.ryuqq.setof.application.cart.validator.CartValidator;
import com.ryuqq.setof.domain.cart.Cart;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * AddCartItemService - 장바구니 항목 추가 Service.
 *
 * <p>재고 검증 후 Factory에서 Cart 도메인 객체를 생성한 후 Coordinator에 위임합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class AddCartItemService implements AddCartItemUseCase {

    private final CartValidator validator;
    private final CartCommandFactory factory;
    private final CartUpsertCoordinator coordinator;

    public AddCartItemService(
            CartValidator validator,
            CartCommandFactory factory,
            CartUpsertCoordinator coordinator) {
        this.validator = validator;
        this.factory = factory;
        this.coordinator = coordinator;
    }

    @Override
    public List<CartItem> execute(AddCartItemCommand command) {
        command.items()
                .forEach(
                        item ->
                                validator.validateStockAvailability(
                                        item.productId(), item.quantity()));
        Cart cart = factory.create(command);
        return coordinator.upsert(cart);
    }
}
