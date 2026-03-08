package com.ryuqq.setof.application.cart.service.command;

import com.ryuqq.setof.application.cart.dto.command.DeleteCartItemsCommand;
import com.ryuqq.setof.application.cart.factory.CartCommandFactory;
import com.ryuqq.setof.application.cart.manager.CartCommandManager;
import com.ryuqq.setof.application.cart.port.in.command.DeleteCartItemsUseCase;
import com.ryuqq.setof.application.cart.validator.CartValidator;
import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * DeleteCartItemsService - 장바구니 항목 삭제 Service.
 *
 * <p>Validator로 조회 → Factory에서 StatusChangeContext 생성 → domain.remove() → Manager.persistAll.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class DeleteCartItemsService implements DeleteCartItemsUseCase {

    private final CartValidator validator;
    private final CartCommandFactory factory;
    private final CartCommandManager commandManager;

    public DeleteCartItemsService(
            CartValidator validator,
            CartCommandFactory factory,
            CartCommandManager commandManager) {
        this.validator = validator;
        this.factory = factory;
        this.commandManager = commandManager;
    }

    @Override
    public int execute(DeleteCartItemsCommand command) {
        List<CartItem> cartItems =
                validator.getExistingCartItems(command.cartIds(), command.userId());
        StatusChangeContext<List<Long>> context = factory.createDeleteContext(command);

        cartItems.forEach(item -> item.remove(context.changedAt()));
        commandManager.persistAll(cartItems);

        return cartItems.size();
    }
}
