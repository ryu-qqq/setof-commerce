package com.ryuqq.setof.api.auth.dto.response;

/**
 * Token API Response
 *
 * <p>로그인/토큰갱신 응답 DTO
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>API 전용 DTO - Application Response와 분리
 * </ul>
 *
 * @param accessToken Access Token (쿠키로도 전달됨)
 * @param expiresIn 만료 시간 (초)
 * @param isNewMember 신규 회원 여부 (OAuth2 전용)
 * @param needsIntegration LOCAL 회원 통합 필요 여부 (OAuth2 전용)
 * @param memberId 회원 ID (통합 필요 시)
 * @author development-team
 * @since 1.0.0
 */
public record TokenApiResponse(
        String accessToken,
        Long expiresIn,
        Boolean isNewMember,
        Boolean needsIntegration,
        String memberId) {

    /**
     * 일반 로그인 응답 생성
     *
     * @param accessToken Access Token
     * @param expiresIn 만료 시간 (초)
     * @return TokenApiResponse
     */
    public static TokenApiResponse of(String accessToken, long expiresIn) {
        return new TokenApiResponse(accessToken, expiresIn, null, null, null);
    }

    /**
     * OAuth2 로그인 응답 생성
     *
     * @param accessToken Access Token
     * @param expiresIn 만료 시간 (초)
     * @param isNewMember 신규 회원 여부
     * @return TokenApiResponse
     */
    public static TokenApiResponse of(String accessToken, long expiresIn, boolean isNewMember) {
        return new TokenApiResponse(accessToken, expiresIn, isNewMember, false, null);
    }

    /**
     * LOCAL 회원 통합 필요 응답 생성
     *
     * @param memberId 회원 ID
     * @return TokenApiResponse
     */
    public static TokenApiResponse needsIntegration(String memberId) {
        return new TokenApiResponse(null, null, null, true, memberId);
    }

    /**
     * Application TokenPairResponse → TokenApiResponse 변환
     *
     * @param tokens Application 토큰 응답
     * @return TokenApiResponse
     */
    public static TokenApiResponse from(
            com.ryuqq.setof.application.member.dto.response.TokenPairResponse tokens) {
        return new TokenApiResponse(
                tokens.accessToken(), tokens.accessTokenExpiresIn(), null, null, null);
    }
}
