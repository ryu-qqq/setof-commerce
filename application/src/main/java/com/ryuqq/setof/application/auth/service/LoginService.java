package com.ryuqq.setof.application.auth.service;

import com.ryuqq.setof.application.auth.dto.command.LoginCommand;
import com.ryuqq.setof.application.auth.dto.response.LoginResult;
import com.ryuqq.setof.application.auth.manager.AuthManager;
import com.ryuqq.setof.application.auth.port.in.LoginUseCase;
import org.springframework.stereotype.Service;

/**
 * 로그인 서비스.
 *
 * <p>LoginUseCase를 구현하며, AuthManager를 통해 인증을 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Service
public class LoginService implements LoginUseCase {

    private final AuthManager authManager;

    public LoginService(AuthManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public LoginResult execute(LoginCommand command) {
        return authManager.login(command.identifier(), command.password());
    }
}
