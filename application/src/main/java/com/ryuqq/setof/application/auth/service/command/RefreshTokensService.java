package com.ryuqq.setof.application.auth.service.command;

import com.ryuqq.setof.application.auth.dto.command.RefreshTokenCommand;
import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.auth.facade.command.RefreshTokenFacade;
import com.ryuqq.setof.application.auth.port.in.command.IssueTokensUseCase;
import com.ryuqq.setof.application.auth.port.in.command.RefreshTokensUseCase;
import com.ryuqq.setof.application.auth.port.in.command.RevokeTokensUseCase;
import com.ryuqq.setof.domain.auth.exception.InvalidRefreshTokenException;
import org.springframework.stereotype.Service;

/**
 * Refresh Tokens Service
 *
 * <p>토큰 갱신 UseCase 구현체
 *
 * <p><strong>처리 순서:</strong>
 *
 * <ol>
 *   <li>Refresh Token으로 회원 ID 조회 (검증)
 *   <li>기존 Refresh Token 무효화
 *   <li>새 토큰 쌍 발급 및 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class RefreshTokensService implements RefreshTokensUseCase {

    private final RefreshTokenFacade refreshTokenFacade;
    private final RevokeTokensUseCase revokeTokensUseCase;
    private final IssueTokensUseCase issueTokensUseCase;

    public RefreshTokensService(
            RefreshTokenFacade refreshTokenFacade,
            RevokeTokensUseCase revokeTokensUseCase,
            IssueTokensUseCase issueTokensUseCase) {
        this.refreshTokenFacade = refreshTokenFacade;
        this.revokeTokensUseCase = revokeTokensUseCase;
        this.issueTokensUseCase = issueTokensUseCase;
    }

    @Override
    public TokenPairResponse execute(RefreshTokenCommand command) {
        String refreshToken = command.refreshToken();

        String memberId =
                refreshTokenFacade
                        .findMemberIdByToken(refreshToken)
                        .orElseThrow(InvalidRefreshTokenException::new);

        revokeTokensUseCase.revokeByToken(refreshToken);

        return issueTokensUseCase.execute(memberId);
    }
}
