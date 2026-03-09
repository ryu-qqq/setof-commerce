package com.ryuqq.setof.application.member.port.in;

import com.ryuqq.setof.application.member.dto.command.WithdrawalCommand;

/**
 * 회원 탈퇴 UseCase.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
public interface WithdrawalUseCase {

    /**
     * 회원 탈퇴를 수행합니다.
     *
     * @param command 탈퇴 Command
     */
    void execute(WithdrawalCommand command);
}
