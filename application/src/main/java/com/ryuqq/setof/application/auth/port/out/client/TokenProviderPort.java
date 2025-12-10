package com.ryuqq.setof.application.auth.port.out.client;

import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;

/**
 * Token Provider Port
 *
 * <p>JWT 토큰 생성을 담당하는 Port-Out 인터페이스
 *
 * <p><strong>구현체:</strong>
 *
 * <ul>
 *   <li>infrastructure: JwtTokenProvider
 * </ul>
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>Access Token 생성
 *   <li>Refresh Token 생성
 *   <li>토큰 검증
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface TokenProviderPort {

    /**
     * 토큰 쌍 생성
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @return Access Token + Refresh Token 쌍
     */
    TokenPairResponse generateTokenPair(String memberId);

    /**
     * Access Token 유효성 검증
     *
     * @param accessToken Access Token 값
     * @return 유효 여부
     */
    boolean validateAccessToken(String accessToken);

    /**
     * Access Token에서 회원 ID 추출
     *
     * @param accessToken Access Token 값
     * @return 회원 ID (UUID 문자열)
     */
    String extractMemberId(String accessToken);

    /**
     * Access Token 만료 여부 확인
     *
     * <p>토큰이 만료되었지만 서명이 유효한 경우 true 반환 (Silent Refresh 대상 판단용)
     *
     * @param accessToken Access Token 값
     * @return 만료 여부
     */
    boolean isAccessTokenExpired(String accessToken);

    /**
     * Refresh Token 유효성 검증
     *
     * @param refreshToken Refresh Token 값
     * @return 유효 여부
     */
    boolean validateRefreshToken(String refreshToken);

    /**
     * Refresh Token에서 회원 ID 추출
     *
     * @param refreshToken Refresh Token 값
     * @return 회원 ID (UUID 문자열)
     */
    String extractMemberIdFromRefreshToken(String refreshToken);
}
