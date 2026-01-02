package com.ryuqq.setof.application.discount.port.in.command;

import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountTargetsCommand;

/**
 * 할인 정책 적용 대상 수정 UseCase (Port-In)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateDiscountTargetsUseCase {

    /**
     * 할인 정책의 적용 대상 ID 목록 수정
     *
     * @param command 적용 대상 수정 커맨드
     */
    void execute(UpdateDiscountTargetsCommand command);
}
