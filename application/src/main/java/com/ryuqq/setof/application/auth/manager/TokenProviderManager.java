package com.ryuqq.setof.application.auth.manager;

import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.auth.port.out.client.TokenProviderPort;
import org.springframework.stereotype.Component;

/**
 * TokenProviderManager - 토큰 생성/검증 Manager.
 *
 * <p>TokenProviderPort를 래핑합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class TokenProviderManager {

    private final TokenProviderPort tokenProviderPort;

    public TokenProviderManager(TokenProviderPort tokenProviderPort) {
        this.tokenProviderPort = tokenProviderPort;
    }

    /**
     * 토큰 쌍을 생성합니다.
     *
     * @param memberId 회원 ID (문자열)
     * @return 생성된 토큰 쌍
     */
    public TokenPairResponse generateTokenPair(String memberId) {
        return tokenProviderPort.generateTokenPair(memberId);
    }

    /**
     * Access Token에서 회원 ID를 추출합니다.
     *
     * @param accessToken Access Token
     * @return 회원 ID
     */
    public String extractMemberId(String accessToken) {
        return tokenProviderPort.extractMemberId(accessToken);
    }

    /**
     * Access Token의 유효성을 검증합니다.
     *
     * @param accessToken Access Token
     * @return 유효 여부
     */
    public boolean validateAccessToken(String accessToken) {
        return tokenProviderPort.validateAccessToken(accessToken);
    }

    /**
     * Refresh Token의 유효성을 검증합니다.
     *
     * @param refreshToken Refresh Token
     * @return 유효 여부
     */
    public boolean validateRefreshToken(String refreshToken) {
        return tokenProviderPort.validateRefreshToken(refreshToken);
    }

    /**
     * Refresh Token에서 회원 ID를 추출합니다.
     *
     * @param refreshToken Refresh Token
     * @return 회원 ID
     */
    public String extractMemberIdFromRefreshToken(String refreshToken) {
        return tokenProviderPort.extractMemberIdFromRefreshToken(refreshToken);
    }

    /**
     * Access Token이 만료되었는지 확인합니다.
     *
     * @param accessToken Access Token
     * @return 만료 여부
     */
    public boolean isAccessTokenExpired(String accessToken) {
        return tokenProviderPort.isAccessTokenExpired(accessToken);
    }
}
