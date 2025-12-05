package com.ryuqq.setof.api.auth.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Login API Request
 *
 * <p>로컬 로그인 요청 DTO
 *
 * <p>Bean Validation:
 *
 * <ul>
 *   <li>phoneNumber: 010으로 시작하는 11자리
 *   <li>password: 필수값
 * </ul>
 *
 * @param phoneNumber 핸드폰 번호
 * @param password 비밀번호
 * @author development-team
 * @since 1.0.0
 */
public record LoginApiRequest(
        @NotBlank(message = "핸드폰 번호는 필수입니다.")
                @Pattern(regexp = "^010\\d{8}$", message = "핸드폰 번호 형식이 올바르지 않습니다.")
                String phoneNumber,
        @NotBlank(message = "비밀번호는 필수입니다.") String password) {}
