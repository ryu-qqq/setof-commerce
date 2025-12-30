package com.ryuqq.setof.application.discount.port.in.command;

import com.ryuqq.setof.application.discount.dto.command.RegisterDiscountPolicyCommand;

/**
 * 할인 정책 등록 UseCase (Port-In)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RegisterDiscountPolicyUseCase {

    /**
     * 할인 정책 등록
     *
     * @param command 등록 커맨드
     * @return 생성된 할인 정책 ID
     */
    Long execute(RegisterDiscountPolicyCommand command);
}
