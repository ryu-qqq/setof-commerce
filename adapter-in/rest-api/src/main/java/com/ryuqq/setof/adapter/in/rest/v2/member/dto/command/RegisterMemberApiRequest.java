package com.ryuqq.setof.adapter.in.rest.v2.member.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Register Member API Request
 *
 * <p>회원가입 요청 DTO
 *
 * <p>Bean Validation:
 *
 * <ul>
 *   <li>csPhoneNumber: 010으로 시작하는 11자리
 *   <li>passwordHash: 8자 이상, 영문+숫자+특수문자 포함
 *   <li>name: 2~50자
 *   <li>consents: 필수 동의 항목
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "회원가입 요청")
public record RegisterMemberApiRequest(
        @Schema(
                        description = "핸드폰 번호 (010으로 시작하는 11자리)",
                        example = "01012345678",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "핸드폰 번호는 필수입니다.")
                @Pattern(regexp = "^010\\d{8}$", message = "핸드폰 번호 형식이 올바르지 않습니다.")
                String phoneNumber,
        @Schema(description = "이메일 주소", example = "user@example.com", nullable = true) String email,
        @Schema(
                        description = "비밀번호 (8자 이상, 영문+숫자+특수문자 포함)",
                        example = "Password1!",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "비밀번호는 필수입니다.")
                @Pattern(
                        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
                        message = "비밀번호는 8자 이상, 영문, 숫자, 특수문자를 포함해야 합니다.")
                String password,
        @Schema(
                        description = "이름 (2~50자)",
                        example = "홍길동",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "이름은 필수입니다.")
                @Size(min = 2, max = 50, message = "이름은 2~50자여야 합니다.")
                String name,
        @Schema(description = "생년월일", example = "1990-01-15", nullable = true)
                LocalDate dateOfBirth,
        @Schema(description = "성별 (MALE, FEMALE)", example = "MALE", nullable = true) String gender,
        @Schema(
                        description = "개인정보 수집 동의",
                        example = "true",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "개인정보 수집 동의는 필수입니다.")
                Boolean privacyConsent,
        @Schema(
                        description = "서비스 이용약관 동의",
                        example = "true",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "서비스 이용약관 동의는 필수입니다.")
                Boolean serviceTermsConsent,
        @Schema(
                        description = "광고 수신 동의 여부",
                        example = "false",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "광고 수신 동의 여부는 필수입니다.")
                Boolean adConsent) {}
