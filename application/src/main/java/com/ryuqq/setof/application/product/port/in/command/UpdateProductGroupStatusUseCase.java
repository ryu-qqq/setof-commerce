package com.ryuqq.setof.application.product.port.in.command;

import com.ryuqq.setof.application.product.dto.command.UpdateProductGroupStatusCommand;

/**
 * Update ProductGroup Status UseCase (Command)
 *
 * <p>상품그룹 상태 변경을 담당하는 Inbound Port
 *
 * <p>비즈니스 로직:
 *
 * <ol>
 *   <li>상품그룹 존재 검증
 *   <li>셀러 소유권 검증
 *   <li>상태 전이 유효성 검증
 *   <li>상태 변경
 *   <li>저장 (트랜잭션)
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateProductGroupStatusUseCase {

    /**
     * 상품그룹 상태 변경 실행
     *
     * @param command 상품그룹 상태 변경 명령
     */
    void execute(UpdateProductGroupStatusCommand command);
}
