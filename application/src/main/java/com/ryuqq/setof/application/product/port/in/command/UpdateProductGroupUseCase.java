package com.ryuqq.setof.application.product.port.in.command;

import com.ryuqq.setof.application.product.dto.command.UpdateProductGroupCommand;

/**
 * Update ProductGroup UseCase (Command)
 *
 * <p>상품그룹 수정을 담당하는 Inbound Port
 *
 * <p>비즈니스 로직:
 *
 * <ol>
 *   <li>상품그룹 존재 검증
 *   <li>셀러 소유권 검증
 *   <li>가격 유효성 검증 (정가 >= 판매가)
 *   <li>ProductGroup 도메인 수정
 *   <li>저장 (트랜잭션)
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateProductGroupUseCase {

    /**
     * 상품그룹 수정 실행
     *
     * @param command 상품그룹 수정 명령
     */
    void execute(UpdateProductGroupCommand command);
}
