package com.ryuqq.setof.application.member.port.in.command;

import com.ryuqq.setof.application.member.dto.command.LocalLoginCommand;
import com.ryuqq.setof.application.member.dto.response.LocalLoginResponse;

/**
 * Local Login UseCase (Command)
 *
 * <p>핸드폰 번호 + 비밀번호 로그인을 담당하는 Inbound Port
 *
 * <p>비즈니스 로직:
 *
 * <ol>
 *   <li>핸드폰 번호로 회원 조회
 *   <li>카카오 회원 확인 → 차단 예외
 *   <li>비밀번호 검증
 *   <li>JWT 토큰 발급
 *   <li>Refresh Token 저장 (Redis + DB)
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface LocalLoginUseCase {

    /**
     * 로컬 로그인 실행
     *
     * @param command 로그인 명령
     * @return 로그인 결과 (memberId, tokens)
     */
    LocalLoginResponse execute(LocalLoginCommand command);
}
