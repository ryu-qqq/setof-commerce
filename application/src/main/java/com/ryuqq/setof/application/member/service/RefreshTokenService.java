package com.ryuqq.setof.application.member.service;

import com.ryuqq.setof.application.member.dto.command.RefreshTokenCommand;
import com.ryuqq.setof.application.member.dto.response.RefreshTokenResponse;
import com.ryuqq.setof.application.member.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.member.manager.TokenManager;
import com.ryuqq.setof.application.member.port.in.command.RefreshTokenUseCase;
import org.springframework.stereotype.Service;

/**
 * 토큰 갱신 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>TokenManager로 Refresh Token 검증 및 토큰 갱신
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class RefreshTokenService implements RefreshTokenUseCase {

    private final TokenManager tokenManager;

    public RefreshTokenService(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public RefreshTokenResponse execute(RefreshTokenCommand command) {
        TokenPairResponse tokenPairResponse = tokenManager.refreshTokens(command.refreshToken());

        return new RefreshTokenResponse(tokenPairResponse);
    }
}
