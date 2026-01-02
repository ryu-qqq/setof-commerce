package com.ryuqq.setof.application.product.port.in.command;

import com.ryuqq.setof.application.product.dto.command.UpdateProductPriceCommand;

/**
 * 상품 가격 수정 UseCase (Command)
 *
 * <p>상품그룹의 가격만 수정하는 Inbound Port
 *
 * <p>비즈니스 로직:
 *
 * <ol>
 *   <li>상품그룹 존재 검증
 *   <li>셀러 소유권 검증 (sellerId가 있는 경우)
 *   <li>가격 유효성 검증 (정가 >= 판매가)
 *   <li>가격 필드만 수정
 *   <li>저장 (트랜잭션)
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateProductPriceUseCase {

    /**
     * 상품 가격 수정 실행
     *
     * @param command 가격 수정 명령
     */
    void execute(UpdateProductPriceCommand command);
}
