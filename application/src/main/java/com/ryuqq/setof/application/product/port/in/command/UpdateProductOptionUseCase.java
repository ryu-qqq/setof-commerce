package com.ryuqq.setof.application.product.port.in.command;

import com.ryuqq.setof.application.product.dto.command.UpdateProductOptionCommand;

/**
 * Update Product Option UseCase (Command)
 *
 * <p>상품(SKU) 옵션 수정을 담당하는 Inbound Port
 *
 * <p>비즈니스 로직:
 *
 * <ol>
 *   <li>상품 존재 검증
 *   <li>셀러 소유권 검증 (ProductGroup 기반)
 *   <li>옵션 정보 수정 (option1, option2, additionalPrice)
 *   <li>재고 수량 수정 (quantity가 있는 경우)
 *   <li>저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateProductOptionUseCase {

    /**
     * 상품 옵션 수정 실행
     *
     * @param command 상품 옵션 수정 명령
     */
    void execute(UpdateProductOptionCommand command);
}
