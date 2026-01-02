package com.ryuqq.setof.application.product.port.in.command;

import com.ryuqq.setof.application.product.dto.command.MarkProductOutOfStockCommand;

/**
 * Mark Product Out Of Stock UseCase (Command)
 *
 * <p>상품(SKU) 품절 상태 변경을 담당하는 Inbound Port
 *
 * <p>비즈니스 로직:
 *
 * <ol>
 *   <li>상품 존재 검증
 *   <li>셀러 소유권 검증 (ProductGroup 기반)
 *   <li>품절 상태 변경 (markSoldOut/markInStock)
 *   <li>저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface MarkProductOutOfStockUseCase {

    /**
     * 상품 품절 상태 변경 실행
     *
     * @param command 상품 품절 상태 변경 명령
     */
    void execute(MarkProductOutOfStockCommand command);
}
