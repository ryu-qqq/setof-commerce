package com.ryuqq.setof.application.auth.dto.response;

/**
 * Token Pair Response
 *
 * <p>Access Token과 Refresh Token 쌍을 담는 응답 DTO
 *
 * @param accessToken Access Token 값
 * @param accessTokenExpiresIn Access Token 만료 시간 (초)
 * @param refreshToken Refresh Token 값
 * @param refreshTokenExpiresIn Refresh Token 만료 시간 (초)
 * @author development-team
 * @since 1.0.0
 */
public record TokenPairResponse(
        String accessToken,
        long accessTokenExpiresIn,
        String refreshToken,
        long refreshTokenExpiresIn) {

    public static TokenPairResponse of(
            String accessToken,
            long accessTokenExpiresIn,
            String refreshToken,
            long refreshTokenExpiresIn) {
        return new TokenPairResponse(
                accessToken, accessTokenExpiresIn, refreshToken, refreshTokenExpiresIn);
    }
}
