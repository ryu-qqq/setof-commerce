package com.ryuqq.setof.application.cart.port.in.command;

import com.ryuqq.setof.application.cart.dto.command.ModifyCartItemCommand;

/**
 * 장바구니 항목 수량 수정 UseCase.
 *
 * <p>레거시 PUT /api/v1/cart/{cartId} 기반. cartId + userId 소유권 검증 포함.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ModifyCartItemUseCase {

    void execute(ModifyCartItemCommand command);
}
