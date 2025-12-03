package com.ryuqq.setof.application.member.port.out;

import com.ryuqq.setof.application.member.dto.response.TokenPairResponse;

/**
 * Token Provider Port
 *
 * <p>JWT 토큰 발급/검증을 위한 Outbound Port
 *
 * <p>구현체는 Adapter 레이어에서 JWT 라이브러리로 구현
 *
 * @author development-team
 * @since 1.0.0
 */
public interface TokenProviderPort {

    /**
     * Access Token + Refresh Token 쌍 생성
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @return 토큰 쌍
     */
    TokenPairResponse generateTokenPair(String memberId);

    /**
     * Access Token에서 회원 ID 추출
     *
     * @param accessToken Access Token
     * @return 회원 ID (UUID 문자열)
     */
    String extractMemberId(String accessToken);

    /**
     * Access Token 유효성 검증
     *
     * @param accessToken Access Token
     * @return 유효 여부
     */
    boolean validateAccessToken(String accessToken);

    /**
     * Refresh Token 유효성 검증
     *
     * @param refreshToken Refresh Token
     * @return 유효 여부
     */
    boolean validateRefreshToken(String refreshToken);
}
