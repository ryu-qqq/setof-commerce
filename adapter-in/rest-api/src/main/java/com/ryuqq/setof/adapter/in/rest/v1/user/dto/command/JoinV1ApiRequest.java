package com.ryuqq.setof.adapter.in.rest.v1.user.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * V1 회원가입 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "회원가입 요청")
public record JoinV1ApiRequest(
        @Schema(
                        description = "핸드폰 번호 (010으로 시작하는 11자리)",
                        example = "01012345678",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "핸드폰 번호는 필수입니다.")
                @Pattern(regexp = "^010\\d{8}$", message = "유효하지 않은 전화번호 형식입니다.")
                String phoneNumber,
        @Schema(
                        description = "비밀번호 (숫자, 영문, 특수문자 포함 8자 이상)",
                        example = "Password1!",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "비밀번호는 필수입니다.")
                @Pattern(
                        regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}",
                        message = "비밀번호는 숫자, 영문, 특수문자를 포함한 8자 이상이어야 합니다.")
                String passwordHash,
        @Schema(description = "유입 경로", example = "kakao") String referer,
        @Schema(
                        description = "이름 (2~5자)",
                        example = "홍길동",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "이름은 필수입니다.")
                @Size(min = 2, max = 5, message = "이름은 2~5자 사이여야 합니다.")
                String name,
        @Schema(
                        description = "개인정보 수집 동의 (Y/N)",
                        example = "Y",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "개인정보 수집 동의는 필수입니다.")
                @Pattern(regexp = "^[YN]$", message = "Y 또는 N만 허용됩니다.")
                String privacyConsent,
        @Schema(
                        description = "서비스 이용약관 동의 (Y/N)",
                        example = "Y",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "서비스 이용약관 동의는 필수입니다.")
                @Pattern(regexp = "^[YN]$", message = "Y 또는 N만 허용됩니다.")
                String serviceTermsConsent,
        @Schema(
                        description = "광고 수신 동의 (Y/N)",
                        example = "N",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "광고 수신 동의는 필수입니다.")
                @Pattern(regexp = "^[YN]$", message = "Y 또는 N만 허용됩니다.")
                String adConsent) {}
