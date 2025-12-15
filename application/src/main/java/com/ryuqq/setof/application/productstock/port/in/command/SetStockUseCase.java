package com.ryuqq.setof.application.productstock.port.in.command;

import com.ryuqq.setof.application.productstock.dto.command.SetStockCommand;

/**
 * 재고 설정 UseCase
 *
 * <p>상품의 재고 수량을 직접 설정합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface SetStockUseCase {

    /**
     * 재고 설정 실행
     *
     * @param command 설정 커맨드
     */
    void execute(SetStockCommand command);
}
