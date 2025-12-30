package com.ryuqq.setof.application.cart.port.in.command;

import com.ryuqq.setof.application.cart.dto.command.ClearCartCommand;
import com.ryuqq.setof.application.cart.dto.response.CartResponse;

/**
 * 장바구니 비우기 UseCase
 *
 * <p>장바구니를 비웁니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ClearCartUseCase {

    /**
     * 장바구니 비우기
     *
     * @param command 장바구니 비우기 Command
     * @return 비워진 장바구니 응답
     */
    CartResponse clear(ClearCartCommand command);
}
