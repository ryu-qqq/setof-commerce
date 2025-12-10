package com.ryuqq.setof.application.auth.dto.command;

/**
 * Refresh Token Command
 *
 * <p>토큰 갱신 요청 Command DTO
 *
 * @param refreshToken 기존 Refresh Token 값
 * @author development-team
 * @since 1.0.0
 */
public record RefreshTokenCommand(String refreshToken) {

    public RefreshTokenCommand {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("refreshToken must not be blank");
        }
    }

    public static RefreshTokenCommand of(String refreshToken) {
        return new RefreshTokenCommand(refreshToken);
    }
}
