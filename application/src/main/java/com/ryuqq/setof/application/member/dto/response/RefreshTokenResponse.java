package com.ryuqq.setof.application.member.dto.response;

import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;

/**
 * Refresh Token Result
 *
 * <p>토큰 갱신 결과 데이터
 *
 * @author development-team
 * @since 1.0.0
 */
public record RefreshTokenResponse(TokenPairResponse tokens) {
    // Immutable value object - no additional behavior
}
