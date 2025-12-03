package com.ryuqq.setof.application.member.port.in.command;

import com.ryuqq.setof.application.member.dto.command.KakaoOAuthCommand;
import com.ryuqq.setof.application.member.dto.response.KakaoOAuthResponse;

/**
 * Kakao OAuth Login UseCase (Command)
 *
 * <p>카카오 로그인을 담당하는 Inbound Port
 *
 * <p>시나리오:
 * <ul>
 *   <li>신규 카카오 회원: 자동 회원가입 후 토큰 발급
 *   <li>기존 카카오 회원: 토큰 발급
 *   <li>기존 LOCAL 회원 (동일 핸드폰): 통합 유도 플래그 반환
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface KakaoOAuthLoginUseCase {

    /**
     * 카카오 로그인 실행
     *
     * @param command 카카오 로그인 커맨드
     * @return 카카오 로그인 응답
     */
    KakaoOAuthResponse execute(KakaoOAuthCommand command);
}
