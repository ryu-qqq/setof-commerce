package com.ryuqq.setof.adapter.in.rest.v1.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 로그인 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "로그인 응답")
public record LoginV1ApiResponse(
        @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                String accessToken,
        @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                String refreshToken,
        @Schema(description = "토큰 타입", example = "Bearer") String tokenType,
        @Schema(description = "만료 시간 (초)", example = "3600") Long expiresIn) {}
