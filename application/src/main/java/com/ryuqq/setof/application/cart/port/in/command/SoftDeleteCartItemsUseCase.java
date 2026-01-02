package com.ryuqq.setof.application.cart.port.in.command;

import com.ryuqq.setof.application.cart.dto.command.SoftDeleteCartItemsCommand;

/**
 * 장바구니 아이템 소프트 딜리트 UseCase
 *
 * <p>체크아웃 생성 시 장바구니 아이템을 소프트 딜리트하기 위한 Port-In 인터페이스입니다.
 *
 * <p>소프트 딜리트된 아이템은 결제 실패 시 복원할 수 있습니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface SoftDeleteCartItemsUseCase {

    /**
     * 장바구니 아이템 소프트 딜리트
     *
     * <p>productStockId를 기준으로 해당 회원의 장바구니 아이템을 소프트 딜리트합니다.
     *
     * @param command 소프트 딜리트 Command
     */
    void softDeleteItems(SoftDeleteCartItemsCommand command);
}
