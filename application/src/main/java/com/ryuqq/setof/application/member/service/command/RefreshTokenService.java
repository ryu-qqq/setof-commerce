package com.ryuqq.setof.application.member.service.command;

import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.auth.port.in.command.RefreshTokensUseCase;
import com.ryuqq.setof.application.member.dto.command.RefreshTokenCommand;
import com.ryuqq.setof.application.member.dto.response.RefreshTokenResponse;
import com.ryuqq.setof.application.member.port.in.command.RefreshTokenUseCase;
import org.springframework.stereotype.Service;

/**
 * 토큰 갱신 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>RefreshTokensUseCase로 Refresh Token 검증 및 토큰 갱신
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class RefreshTokenService implements RefreshTokenUseCase {

    private final RefreshTokensUseCase refreshTokensUseCase;

    public RefreshTokenService(RefreshTokensUseCase refreshTokensUseCase) {
        this.refreshTokensUseCase = refreshTokensUseCase;
    }

    @Override
    public RefreshTokenResponse execute(RefreshTokenCommand command) {
        com.ryuqq.setof.application.auth.dto.command.RefreshTokenCommand authCommand =
                com.ryuqq.setof.application.auth.dto.command.RefreshTokenCommand.of(
                        command.refreshToken());

        TokenPairResponse tokenPairResponse = refreshTokensUseCase.execute(authCommand);

        return new RefreshTokenResponse(tokenPairResponse);
    }
}
