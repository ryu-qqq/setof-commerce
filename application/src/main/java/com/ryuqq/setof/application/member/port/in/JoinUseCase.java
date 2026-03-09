package com.ryuqq.setof.application.member.port.in;

import com.ryuqq.setof.application.auth.dto.response.LoginResult;
import com.ryuqq.setof.application.member.dto.command.JoinCommand;

/**
 * 회원 가입 UseCase.
 *
 * <p>회원 등록 후 자동 로그인하여 토큰을 발급합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
public interface JoinUseCase {

    /**
     * 회원 가입을 수행합니다.
     *
     * @param command 가입 Command
     * @return 로그인 결과 (토큰 정보 포함)
     */
    LoginResult execute(JoinCommand command);
}
