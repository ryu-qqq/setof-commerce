package com.ryuqq.setof.application.product.port.in.command;

import com.ryuqq.setof.application.product.dto.command.DeleteProductGroupCommand;

/**
 * Delete ProductGroup UseCase (Command)
 *
 * <p>상품그룹 삭제를 담당하는 Inbound Port
 *
 * <p>비즈니스 로직:
 *
 * <ol>
 *   <li>상품그룹 존재 검증
 *   <li>셀러 소유권 검증
 *   <li>Soft Delete 처리
 *   <li>저장 (트랜잭션)
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeleteProductGroupUseCase {

    /**
     * 상품그룹 삭제 실행
     *
     * @param command 상품그룹 삭제 명령
     */
    void execute(DeleteProductGroupCommand command);
}
