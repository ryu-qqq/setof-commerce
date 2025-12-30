package com.ryuqq.setof.application.discount.port.in.command;

import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyCommand;

/**
 * 할인 정책 수정 UseCase (Port-In)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateDiscountPolicyUseCase {

    /**
     * 할인 정책 수정
     *
     * @param command 수정 커맨드
     */
    void execute(UpdateDiscountPolicyCommand command);
}
