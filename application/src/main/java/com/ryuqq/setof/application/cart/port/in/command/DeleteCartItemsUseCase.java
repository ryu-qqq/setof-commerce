package com.ryuqq.setof.application.cart.port.in.command;

import com.ryuqq.setof.application.cart.dto.command.DeleteCartItemsCommand;

/**
 * 장바구니 항목 삭제 UseCase.
 *
 * <p>레거시 DELETE /api/v1/carts 기반. 소프트 딜리트(delete_yn = 'Y').
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface DeleteCartItemsUseCase {

    int execute(DeleteCartItemsCommand command);
}
