package com.ryuqq.setof.application.cart.service.command;

import com.ryuqq.setof.application.cart.dto.command.ModifyCartItemCommand;
import com.ryuqq.setof.application.cart.factory.CartCommandFactory;
import com.ryuqq.setof.application.cart.manager.CartCommandManager;
import com.ryuqq.setof.application.cart.port.in.command.ModifyCartItemUseCase;
import com.ryuqq.setof.application.cart.validator.CartValidator;
import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import com.ryuqq.setof.domain.cart.id.CartItemId;
import com.ryuqq.setof.domain.cart.vo.CartItemUpdateData;
import org.springframework.stereotype.Service;

/**
 * ModifyCartItemService - 장바구니 항목 수량 수정 Service.
 *
 * <p>Validator로 조회 → Factory에서 UpdateContext 생성 → domain.update() → Manager.persist.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class ModifyCartItemService implements ModifyCartItemUseCase {

    private final CartValidator validator;
    private final CartCommandFactory factory;
    private final CartCommandManager commandManager;

    public ModifyCartItemService(
            CartValidator validator,
            CartCommandFactory factory,
            CartCommandManager commandManager) {
        this.validator = validator;
        this.factory = factory;
        this.commandManager = commandManager;
    }

    @Override
    public void execute(ModifyCartItemCommand command) {
        CartItem cartItem = validator.getExistingCartItem(command.cartId(), command.userId());
        validator.validateStockAvailability(cartItem.productIdValue(), command.newQuantity());
        UpdateContext<CartItemId, CartItemUpdateData> context =
                factory.createUpdateContext(command);
        cartItem.update(context.updateData());
        commandManager.persist(cartItem);
    }
}
