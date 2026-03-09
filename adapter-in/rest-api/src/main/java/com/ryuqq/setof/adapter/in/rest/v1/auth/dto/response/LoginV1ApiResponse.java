package com.ryuqq.setof.adapter.in.rest.v1.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * LoginV1ApiResponse - 로그인 V1 응답 DTO.
 *
 * <p>API-DTO-003: record 기반 Response DTO.
 *
 * <p>레거시 TokenDto 호환: accessToken + refreshToken.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Schema(description = "로그인 응답")
public record LoginV1ApiResponse(
        @Schema(description = "액세스 토큰") String accessToken,
        @Schema(description = "리프레시 토큰") String refreshToken) {}
