package com.ryuqq.setof.application.auth.service;

import com.ryuqq.setof.application.auth.dto.command.LoginCommand;
import com.ryuqq.setof.application.auth.dto.response.LoginResult;
import com.ryuqq.setof.application.auth.manager.TokenCommandFacade;
import com.ryuqq.setof.application.auth.port.in.LoginUseCase;
import com.ryuqq.setof.application.member.validator.LoginValidator;
import com.ryuqq.setof.domain.member.aggregate.Member;
import org.springframework.stereotype.Service;

/**
 * 로그인 서비스.
 *
 * <p>LoginUseCase를 구현하며, LoginValidator로 검증 후 TokenManager로 토큰을 발급합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Service
public class LoginService implements LoginUseCase {

    private final LoginValidator loginValidator;
    private final TokenCommandFacade tokenCommandFacade;

    public LoginService(LoginValidator loginValidator, TokenCommandFacade tokenCommandFacade) {
        this.loginValidator = loginValidator;
        this.tokenCommandFacade = tokenCommandFacade;
    }

    @Override
    public LoginResult execute(LoginCommand command) {
        Member member = loginValidator.validate(command.identifier(), command.password());
        return tokenCommandFacade.issueLoginResult(member.legacyMemberIdValue());
    }
}
