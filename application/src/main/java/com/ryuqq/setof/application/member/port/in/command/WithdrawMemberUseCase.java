package com.ryuqq.setof.application.member.port.in.command;

import com.ryuqq.setof.application.member.dto.command.WithdrawMemberCommand;

/**
 * Withdraw Member UseCase (Command)
 *
 * <p>회원 탈퇴를 담당하는 Inbound Port
 *
 * <p>비즈니스 로직:
 *
 * <ol>
 *   <li>회원 조회
 *   <li>상태 → WITHDRAWN
 *   <li>탈퇴 사유 저장
 *   <li>Refresh Token 무효화
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface WithdrawMemberUseCase {

    /**
     * 회원 탈퇴 실행
     *
     * @param command 회원 탈퇴 명령
     */
    void execute(WithdrawMemberCommand command);
}
