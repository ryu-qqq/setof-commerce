package com.ryuqq.setof.application.cart.port.in.command;

import com.ryuqq.setof.application.cart.dto.command.UpdateCartItemQuantityCommand;
import com.ryuqq.setof.application.cart.dto.response.CartResponse;

/**
 * 장바구니 아이템 수량 변경 UseCase
 *
 * <p>장바구니 아이템의 수량을 변경합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateCartItemQuantityUseCase {

    /**
     * 아이템 수량 변경
     *
     * @param command 수량 변경 Command
     * @return 업데이트된 장바구니 응답
     */
    CartResponse updateQuantity(UpdateCartItemQuantityCommand command);
}
