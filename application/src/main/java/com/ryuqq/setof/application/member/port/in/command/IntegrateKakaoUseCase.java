package com.ryuqq.setof.application.member.port.in.command;

import com.ryuqq.setof.application.member.dto.command.IntegrateKakaoCommand;

/**
 * Integrate Kakao UseCase (Command)
 *
 * <p>기존 LOCAL 회원에 카카오 계정을 통합하는 Inbound Port
 *
 * <p>비즈니스 로직:
 * <ol>
 *   <li>회원 조회
 *   <li>provider: LOCAL → KAKAO 변경
 *   <li>socialId 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface IntegrateKakaoUseCase {

    /**
     * 카카오 계정 통합 실행
     *
     * @param command 카카오 통합 커맨드
     */
    void execute(IntegrateKakaoCommand command);
}
