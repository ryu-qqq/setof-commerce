package com.ryuqq.setof.adapter.in.rest.auth.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
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
 *   <li>csPhoneNumber: 010으로 시작하는 11자리
 *   <li>passwordHash: 필수값
 * </ul>
 *
 * @param phoneNumber 핸드폰 번호
 * @param password 비밀번호
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "로그인 요청")
public record LoginApiRequest(
        @Schema(
                        description = "핸드폰 번호 (010으로 시작하는 11자리)",
                        example = "01012345678",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "핸드폰 번호는 필수입니다.")
                @Pattern(regexp = "^010\\d{8}$", message = "핸드폰 번호 형식이 올바르지 않습니다.")
                String phoneNumber,
        @Schema(
                        description = "비밀번호",
                        example = "Password1!",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "비밀번호는 필수입니다.")
                String password) {}
