package com.ryuqq.setof.application.member.port.in;

import com.ryuqq.setof.application.auth.dto.response.KakaoLoginResult;
import com.ryuqq.setof.application.member.dto.command.KakaoLoginCommand;

/**
 * 카카오 소셜 로그인 UseCase.
 *
 * <p>카카오 OAuth2 인증 완료 후 회원 조회/생성/통합 및 토큰 발급을 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
public interface KakaoLoginUseCase {

    /**
     * 카카오 로그인 실행.
     *
     * @param command 카카오 로그인 Command
     * @return 카카오 로그인 결과 (토큰 + joined 여부)
     */
    KakaoLoginResult execute(KakaoLoginCommand command);
}
