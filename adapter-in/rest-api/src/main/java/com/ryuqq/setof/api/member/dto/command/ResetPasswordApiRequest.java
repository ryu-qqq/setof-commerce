package com.ryuqq.setof.api.member.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Reset Password API Request
 *
 * <p>비밀번호 재설정 요청 DTO
 *
 * @param phoneNumber 핸드폰 번호
 * @param newPassword 새 비밀번호
 * @author development-team
 * @since 1.0.0
 */
public record ResetPasswordApiRequest(
        @NotBlank(message = "핸드폰 번호는 필수입니다.")
                @Pattern(regexp = "^010\\d{8}$", message = "핸드폰 번호 형식이 올바르지 않습니다.")
                String phoneNumber,
        @NotBlank(message = "새 비밀번호는 필수입니다.")
                @Pattern(
                        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
                        message = "비밀번호는 8자 이상, 영문, 숫자, 특수문자를 포함해야 합니다.")
                String newPassword) {}
