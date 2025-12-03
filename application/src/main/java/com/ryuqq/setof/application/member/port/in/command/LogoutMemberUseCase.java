package com.ryuqq.setof.application.member.port.in.command;

import com.ryuqq.setof.application.member.dto.command.LogoutMemberCommand;

/**
 * Logout Member UseCase (Command)
 *
 * <p>로그아웃을 담당하는 Inbound Port
 *
 * <p>비즈니스 로직:
 *
 * <ol>
 *   <li>Redis에서 Refresh Token 삭제
 *   <li>DB에서 Refresh Token 삭제
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface LogoutMemberUseCase {

    /**
     * 로그아웃 실행
     *
     * @param command 로그아웃 명령
     */
    void execute(LogoutMemberCommand command);
}
