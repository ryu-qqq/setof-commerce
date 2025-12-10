package com.ryuqq.setof.adapter.in.rest.v1.user.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * V1 로그인 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "로그인 요청")
public record LoginV1ApiRequest(
        @Schema(
                        description = "핸드폰 번호 (010으로 시작하는 11자리)",
                        example = "01012345678",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "핸드폰 번호는 필수입니다.")
                @Pattern(regexp = "^010\\d{8}$", message = "유효하지 않은 전화번호 형식입니다.")
                String phoneNumber,
        @Schema(
                        description = "비밀번호",
                        example = "Password1!",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "비밀번호는 필수입니다.")
                String passwordHash,
        @Schema(description = "유입 경로", example = "kakao") String referer) {}
