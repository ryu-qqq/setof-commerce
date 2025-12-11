package com.ryuqq.setof.adapter.in.rest.v2.member.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * Kakao Link API Request
 *
 * <p>카카오 계정 연동 요청 DTO
 *
 * @param kakaoId 카카오 ID
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "카카오 계정 연동 요청")
public record KakaoLinkApiRequest(
        @Schema(
                        description = "카카오 ID",
                        example = "1234567890",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "카카오 ID는 필수입니다.")
                String kakaoId) {}
