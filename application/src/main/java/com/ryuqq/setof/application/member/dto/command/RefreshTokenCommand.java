package com.ryuqq.setof.application.member.dto.command;

/**
 * Refresh Token Command
 *
 * <p>토큰 갱신 요청 데이터를 담는 순수한 불변 객체
 *
 * @author development-team
 * @since 1.0.0
 */
public record RefreshTokenCommand(String refreshToken) {
    // Immutable command object - no additional behavior
}
