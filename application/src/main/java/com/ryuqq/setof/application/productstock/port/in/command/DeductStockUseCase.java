package com.ryuqq.setof.application.productstock.port.in.command;

import com.ryuqq.setof.application.productstock.dto.command.DeductStockCommand;

/**
 * 재고 차감 UseCase
 *
 * <p>주문 등에 의해 재고를 차감합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeductStockUseCase {

    /**
     * 재고 차감 실행
     *
     * @param command 차감 커맨드
     */
    void execute(DeductStockCommand command);
}
