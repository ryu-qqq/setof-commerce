package com.ryuqq.setof.application.discount.port.in.command;

import com.ryuqq.setof.application.discount.dto.command.DeleteDiscountPolicyCommand;

/**
 * 할인 정책 삭제 UseCase (Port-In)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeleteDiscountPolicyUseCase {

    /**
     * 할인 정책 삭제 (Soft Delete)
     *
     * @param command 삭제 커맨드
     */
    void execute(DeleteDiscountPolicyCommand command);
}
