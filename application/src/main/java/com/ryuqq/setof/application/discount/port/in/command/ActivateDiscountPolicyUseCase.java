package com.ryuqq.setof.application.discount.port.in.command;

import com.ryuqq.setof.application.discount.dto.command.ActivateDiscountPolicyCommand;

/**
 * 할인 정책 활성화 UseCase (Port-In)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ActivateDiscountPolicyUseCase {

    /**
     * 할인 정책 활성화
     *
     * @param command 활성화 커맨드
     */
    void execute(ActivateDiscountPolicyCommand command);
}
