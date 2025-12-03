package com.ryuqq.setof.application.member.dto.response;

/**
 * Token Pair Response
 *
 * <p>Access Token + Refresh Token 쌍
 *
 * @author development-team
 * @since 1.0.0
 */
public record TokenPairResponse(
        String accessToken,
        String refreshToken,
        long accessTokenExpiresIn,
        long refreshTokenExpiresIn) {}
