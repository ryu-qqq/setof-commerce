package com.ryuqq.setof.application.auth.manager;

import com.ryuqq.setof.application.auth.dto.response.LoginResult;
import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * TokenManager - 토큰 관련 Facade.
 *
 * <p>TokenProviderManager, RefreshTokenCacheCommandManager, RefreshTokenCacheReadManager를 조합하여 토큰
 * 발급/검증/캐시 관리를 통합 제공합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class TokenCommandFacade {

    private final TokenProviderManager tokenProviderManager;
    private final RefreshTokenCacheCommandManager refreshTokenCacheCommandManager;
    private final RefreshTokenCacheReadManager refreshTokenCacheReadManager;

    public TokenCommandFacade(
            TokenProviderManager tokenProviderManager,
            RefreshTokenCacheCommandManager refreshTokenCacheCommandManager,
            RefreshTokenCacheReadManager refreshTokenCacheReadManager) {
        this.tokenProviderManager = tokenProviderManager;
        this.refreshTokenCacheCommandManager = refreshTokenCacheCommandManager;
        this.refreshTokenCacheReadManager = refreshTokenCacheReadManager;
    }

    /**
     * 토큰 쌍을 생성하고 Refresh Token을 캐시에 저장합니다.
     *
     * @param memberId 회원 ID (문자열)
     * @return 생성된 토큰 쌍
     */
    public TokenPairResponse issueTokenPair(String memberId) {
        TokenPairResponse tokenPair = tokenProviderManager.generateTokenPair(memberId);
        refreshTokenCacheCommandManager.persist(
                tokenPair.refreshToken(), memberId, tokenPair.refreshTokenExpiresIn());
        return tokenPair;
    }

    /**
     * 토큰을 발급하고 LoginResult를 생성합니다.
     *
     * @param userId 레거시 user_id
     * @return 로그인 결과 (토큰 포함)
     */
    public LoginResult issueLoginResult(long userId) {
        String memberId = String.valueOf(userId);
        TokenPairResponse tokenPair = issueTokenPair(memberId);
        return LoginResult.success(
                memberId,
                tokenPair.accessToken(),
                tokenPair.refreshToken(),
                tokenPair.accessTokenExpiresIn(),
                "Bearer");
    }

    /**
     * Access Token에서 회원 ID를 추출합니다.
     *
     * @param accessToken Access Token
     * @return 회원 ID
     */
    public String extractMemberId(String accessToken) {
        return tokenProviderManager.extractMemberId(accessToken);
    }

    /**
     * Refresh Token으로 캐시에서 회원 ID를 조회합니다.
     *
     * @param refreshToken Refresh Token
     * @return 회원 ID (Optional)
     */
    public Optional<String> findMemberIdByRefreshToken(String refreshToken) {
        return refreshTokenCacheReadManager.findMemberIdByRefreshToken(refreshToken);
    }

    /**
     * Refresh Token 캐시를 삭제합니다.
     *
     * @param refreshToken Refresh Token
     */
    public void revokeRefreshToken(String refreshToken) {
        refreshTokenCacheCommandManager.delete(refreshToken);
    }
}
