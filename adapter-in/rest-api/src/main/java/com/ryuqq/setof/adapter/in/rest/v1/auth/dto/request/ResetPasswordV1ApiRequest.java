package com.ryuqq.setof.adapter.in.rest.v1.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * ResetPasswordV1ApiRequest - 비밀번호 재설정 V1 요청 DTO.
 *
 * <p>API-DTO-001: record 기반 Request DTO.
 *
 * <p>API-DTO-002: Jakarta Validation + Swagger 어노테이션.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Schema(description = "비밀번호 재설정 요청")
public record ResetPasswordV1ApiRequest(
        @Schema(description = "전화번호", example = "01012345678")
                @NotBlank(message = "전화번호는 필수입니다.")
                @Pattern(regexp = "010[0-9]{8}", message = "유효하지 않은 전화번호 형식입니다.")
                String phoneNumber,
        @Schema(description = "새 비밀번호")
                @NotBlank(message = "새 비밀번호는 필수입니다.")
                @Pattern(
                        regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}",
                        message = "비밀번호는 숫자, 영문, 특수문자를 포함하여 8자 이상이어야 합니다.")
                String passwordHash) {}
