package com.ryuqq.setof.application.member.manager;

import com.ryuqq.setof.application.member.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.member.port.out.TokenProviderPort;
import com.ryuqq.setof.domain.core.member.exception.InvalidRefreshTokenException;
import org.springframework.stereotype.Component;

/**
 * Token Manager
 *
 * <p>JWT 토큰 발급 및 Refresh Token 저장을 담당
 *
 * <p>주의: 이 클래스의 메서드들은 트랜잭션 외부에서 호출해야 함. JWT 발급/Redis 저장은 외부 시스템 호출이므로 트랜잭션 내에서 수행하면 안됨.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class TokenManager {

    private final TokenProviderPort tokenProviderPort;
    private final RefreshTokenPersistenceManager refreshTokenPersistenceManager;
    private final RefreshTokenCacheManager refreshTokenCacheManager;

    public TokenManager(
            TokenProviderPort tokenProviderPort,
            RefreshTokenPersistenceManager refreshTokenPersistenceManager,
            RefreshTokenCacheManager refreshTokenCacheManager) {
        this.tokenProviderPort = tokenProviderPort;
        this.refreshTokenPersistenceManager = refreshTokenPersistenceManager;
        this.refreshTokenCacheManager = refreshTokenCacheManager;
    }

    /**
     * 토큰 쌍 발급 및 Refresh Token 저장
     *
     * <p>순서:
     *
     * <ol>
     *   <li>JWT Access Token + Refresh Token 생성
     *   <li>Refresh Token → RDB 저장 (트랜잭션)
     *   <li>Refresh Token → Cache(Redis) 저장
     * </ol>
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @return 발급된 토큰 쌍
     */
    public TokenPairResponse issueTokens(String memberId) {
        TokenPairResponse tokenPairResponse = tokenProviderPort.generateTokenPair(memberId);

        saveRefreshToken(memberId, tokenPairResponse.refreshToken(), tokenPairResponse.refreshTokenExpiresIn());

        return tokenPairResponse;
    }

    /**
     * Refresh Token 무효화 (로그아웃 시)
     *
     * @param memberId 회원 ID (UUID 문자열)
     */
    public void revokeTokensByMemberId(String memberId) {
        refreshTokenCacheManager.deleteByMemberId(memberId);
        refreshTokenPersistenceManager.deleteByMemberId(memberId);
    }

    /**
     * Refresh Token 무효화 (토큰 기준)
     *
     * @param refreshToken Refresh Token 값
     */
    public void revokeToken(String refreshToken) {
        refreshTokenCacheManager.deleteByToken(refreshToken);
        refreshTokenPersistenceManager.deleteByToken(refreshToken);
    }

    /**
     * Refresh Token으로 토큰 갱신
     *
     * <p>순서:
     *
     * <ol>
     *   <li>Refresh Token으로 회원 ID 조회 (검증)
     *   <li>기존 Refresh Token 무효화
     *   <li>새 토큰 쌍 발급 및 저장
     * </ol>
     *
     * @param refreshToken 기존 Refresh Token
     * @return 새로 발급된 토큰 쌍
     * @throws InvalidRefreshTokenException 유효하지 않은 Refresh Token인 경우
     */
    public TokenPairResponse refreshTokens(String refreshToken) {
        String memberId =
                refreshTokenCacheManager
                        .findMemberIdByToken(refreshToken)
                        .orElseThrow(InvalidRefreshTokenException::new);

        revokeToken(refreshToken);

        return issueTokens(memberId);
    }

    private void saveRefreshToken(String memberId, String refreshToken, long expiresInSeconds) {
        refreshTokenPersistenceManager.save(memberId, refreshToken, expiresInSeconds);
        refreshTokenCacheManager.save(memberId, refreshToken, expiresInSeconds);
    }
}
