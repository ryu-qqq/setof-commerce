package com.ryuqq.setof.application.discount.port.in.command;

import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyStatusCommand;

/**
 * 할인 정책 사용 상태 일괄 변경 UseCase.
 *
 * <p>복수의 할인 정책을 일괄로 활성화하거나 비활성화합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface UpdateDiscountPolicyStatusUseCase {

    /**
     * 할인 정책 활성/비활성 상태를 일괄 변경합니다.
     *
     * @param command 상태 변경 대상 정책 ID 목록 및 변경 방향
     */
    void execute(UpdateDiscountPolicyStatusCommand command);
}
