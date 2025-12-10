package com.ryuqq.setof.adapter.in.rest.v1.user.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * V1 비밀번호 변경 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "비밀번호 변경 요청")
public record PasswordV1ApiRequest(
        @Schema(
                        description = "핸드폰 번호 (010으로 시작하는 11자리)",
                        example = "01012345678",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "핸드폰 번호는 필수입니다.")
                @Pattern(regexp = "^010\\d{8}$", message = "유효하지 않은 전화번호 형식입니다.")
                String phoneNumber,
        @Schema(
                        description = "새 비밀번호 (숫자, 영문, 특수문자 포함 8자 이상)",
                        example = "NewPassword1!",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "새 비밀번호는 필수입니다.")
                @Pattern(
                        regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}",
                        message = "비밀번호는 숫자, 영문, 특수문자를 포함한 8자 이상이어야 합니다.")
                String passwordHash,
        @Schema(description = "유입 경로", example = "kakao") String referer) {}
