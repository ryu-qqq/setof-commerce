package com.ryuqq.setof.application.productstock.port.in.command;

import com.ryuqq.setof.application.productstock.dto.command.InitializeStockCommand;

/**
 * 재고 초기화 UseCase
 *
 * <p>새 상품의 재고를 초기화합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface InitializeStockUseCase {

    /**
     * 재고 초기화 실행
     *
     * @param command 초기화 커맨드
     * @return 생성된 재고 ID
     */
    Long execute(InitializeStockCommand command);
}
