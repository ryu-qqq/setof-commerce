package com.ryuqq.setof.application.auth.service.command;

import com.ryuqq.setof.application.auth.facade.command.RefreshTokenFacade;
import com.ryuqq.setof.application.auth.port.in.command.RevokeTokensUseCase;
import org.springframework.stereotype.Service;

/**
 * Revoke Tokens Service
 *
 * <p>토큰 무효화 UseCase 구현체
 *
 * <p><strong>사용 시점:</strong>
 *
 * <ul>
 *   <li>로그아웃 시 회원의 모든 토큰 무효화
 *   <li>회원 탈퇴 시 모든 토큰 무효화
 *   <li>토큰 갱신 시 기존 토큰 무효화
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class RevokeTokensService implements RevokeTokensUseCase {

    private final RefreshTokenFacade refreshTokenFacade;

    public RevokeTokensService(RefreshTokenFacade refreshTokenFacade) {
        this.refreshTokenFacade = refreshTokenFacade;
    }

    @Override
    public void revokeByMemberId(String memberId) {
        refreshTokenFacade.deleteByMemberId(memberId);
    }

    @Override
    public void revokeByToken(String refreshToken) {
        refreshTokenFacade.deleteByToken(refreshToken);
    }
}
