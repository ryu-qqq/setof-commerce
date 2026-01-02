package com.ryuqq.setof.application.cart.factory.command;

import com.ryuqq.setof.application.cart.dto.command.AddCartItemCommand;
import com.ryuqq.setof.domain.cart.vo.CartItem;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * Cart Command Factory
 *
 * <p>Command DTO를 Domain 객체로 변환하는 Factory
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CartCommandFactory {

    private final ClockHolder clockHolder;

    public CartCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * AddCartItemCommand를 CartItem으로 변환
     *
     * @param command 아이템 추가 Command
     * @return CartItem Domain VO
     */
    public CartItem toCartItem(AddCartItemCommand command) {
        return CartItem.forNew(
                command.productStockId(),
                command.productId(),
                command.productGroupId(),
                command.sellerId(),
                command.quantity(),
                command.unitPrice(),
                Instant.now(clockHolder.getClock()));
    }
}
