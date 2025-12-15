package com.ryuqq.setof.application.productstock.port.in.command;

import com.ryuqq.setof.application.productstock.dto.command.RestoreStockCommand;

/**
 * 재고 복원 UseCase
 *
 * <p>주문 취소 등에 의해 재고를 복원합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RestoreStockUseCase {

    /**
     * 재고 복원 실행
     *
     * @param command 복원 커맨드
     */
    void execute(RestoreStockCommand command);
}
