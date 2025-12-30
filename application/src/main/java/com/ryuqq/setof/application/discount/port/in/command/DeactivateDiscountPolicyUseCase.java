package com.ryuqq.setof.application.discount.port.in.command;

import com.ryuqq.setof.application.discount.dto.command.DeactivateDiscountPolicyCommand;

/**
 * 할인 정책 비활성화 UseCase (Port-In)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeactivateDiscountPolicyUseCase {

    /**
     * 할인 정책 비활성화
     *
     * @param command 비활성화 커맨드
     */
    void execute(DeactivateDiscountPolicyCommand command);
}
