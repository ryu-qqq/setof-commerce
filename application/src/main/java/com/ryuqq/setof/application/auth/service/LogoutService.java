package com.ryuqq.setof.application.auth.service;

import com.ryuqq.setof.application.auth.dto.command.LogoutCommand;
import com.ryuqq.setof.application.auth.manager.TokenCommandFacade;
import com.ryuqq.setof.application.auth.port.in.LogoutUseCase;
import org.springframework.stereotype.Service;

/**
 * 로그아웃 서비스.
 *
 * <p>LogoutUseCase를 구현하며, TokenManager를 통해 Refresh Token 캐시를 삭제합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Service
public class LogoutService implements LogoutUseCase {

    private final TokenCommandFacade tokenCommandFacade;

    public LogoutService(TokenCommandFacade tokenCommandFacade) {
        this.tokenCommandFacade = tokenCommandFacade;
    }

    @Override
    public void execute(LogoutCommand command) {
        tokenCommandFacade.revokeRefreshToken(command.userId());
    }
}
