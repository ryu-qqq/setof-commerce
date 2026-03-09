package com.ryuqq.setof.application.member.port.in;

import com.ryuqq.setof.application.member.dto.command.ResetPasswordCommand;

/**
 * 비밀번호 재설정 UseCase.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
public interface ResetPasswordUseCase {

    /**
     * 비밀번호를 재설정합니다.
     *
     * @param command 비밀번호 재설정 Command
     */
    void execute(ResetPasswordCommand command);
}
