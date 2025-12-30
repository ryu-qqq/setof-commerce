package com.ryuqq.setof.application.product.port.in.command;

import com.ryuqq.setof.application.product.dto.command.UpdateProductDisplayCommand;

/**
 * Update Product Display UseCase (Command)
 *
 * <p>상품(SKU) 전시 상태 변경을 담당하는 Inbound Port
 *
 * <p>비즈니스 로직:
 *
 * <ol>
 *   <li>상품 존재 검증
 *   <li>셀러 소유권 검증 (ProductGroup 기반)
 *   <li>전시 상태 변경 (show/hide)
 *   <li>저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateProductDisplayUseCase {

    /**
     * 상품 전시 상태 변경 실행
     *
     * @param command 상품 전시 상태 변경 명령
     */
    void execute(UpdateProductDisplayCommand command);
}
