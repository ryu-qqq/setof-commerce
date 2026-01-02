package com.ryuqq.setof.application.cart.port.in.command;

import com.ryuqq.setof.application.cart.dto.command.AddCartItemCommand;
import com.ryuqq.setof.application.cart.dto.response.CartResponse;

/**
 * 장바구니 아이템 추가 UseCase
 *
 * <p>장바구니에 아이템을 추가합니다. 동일 상품이 이미 있으면 수량이 합산됩니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface AddCartItemUseCase {

    /**
     * 장바구니에 아이템 추가
     *
     * @param command 아이템 추가 Command
     * @return 업데이트된 장바구니 응답
     */
    CartResponse addItem(AddCartItemCommand command);
}
