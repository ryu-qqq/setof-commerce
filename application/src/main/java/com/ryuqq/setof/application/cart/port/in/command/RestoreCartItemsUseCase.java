package com.ryuqq.setof.application.cart.port.in.command;

import com.ryuqq.setof.application.cart.dto.command.RestoreCartItemsCommand;

/**
 * 장바구니 아이템 복원 UseCase
 *
 * <p>결제 실패 시 소프트 딜리트된 장바구니 아이템을 복원하기 위한 Port-In 인터페이스입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RestoreCartItemsUseCase {

    /**
     * 소프트 딜리트된 장바구니 아이템 복원
     *
     * <p>productStockId를 기준으로 해당 회원의 삭제된 장바구니 아이템을 찾아 복원합니다.
     *
     * @param command 복원 Command
     */
    void restoreItems(RestoreCartItemsCommand command);
}
