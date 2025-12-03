package com.ryuqq.setof.application.member.port.in.command;

import com.ryuqq.setof.application.member.dto.command.ResetPasswordCommand;

/**
 * Reset Password UseCase (Command)
 *
 * <p>비밀번호 재설정을 담당하는 Inbound Port
 *
 * <p>비즈니스 로직:
 *
 * <ol>
 *   <li>핸드폰 번호로 회원 조회
 *   <li>카카오 회원 → 비밀번호 변경 불가 예외
 *   <li>비밀번호 해시화 후 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ResetPasswordUseCase {

    /**
     * 비밀번호 재설정 실행
     *
     * @param command 비밀번호 재설정 명령
     */
    void execute(ResetPasswordCommand command);
}
