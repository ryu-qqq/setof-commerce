package com.ryuqq.setof.adapter.in.rest.v1.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * JoinV1ApiRequest - 회원가입 V1 요청 DTO.
 *
 * <p>API-DTO-001: record 기반 Request DTO.
 *
 * <p>API-DTO-002: Jakarta Validation + Swagger 어노테이션.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Schema(description = "회원가입 요청")
public record JoinV1ApiRequest(
        @Schema(description = "전화번호", example = "01012345678")
                @NotBlank(message = "전화번호는 필수입니다.")
                @Pattern(regexp = "010[0-9]{8}", message = "유효하지 않은 전화번호 형식입니다.")
                String phoneNumber,
        @Schema(description = "비밀번호")
                @NotBlank(message = "비밀번호는 필수입니다.")
                @Pattern(
                        regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}",
                        message = "비밀번호는 숫자, 영문, 특수문자를 포함하여 8자 이상이어야 합니다.")
                String passwordHash,
        @Schema(description = "이름", example = "홍길동")
                @NotBlank(message = "이름은 필수입니다.")
                @Size(min = 2, max = 5, message = "이름은 2~5자 사이여야 합니다.")
                String name,
        @Schema(description = "개인정보 수집 동의") @NotNull(message = "개인정보 수집 동의 값은 필수입니다.")
                Boolean privacyConsent,
        @Schema(description = "서비스 이용약관 동의") @NotNull(message = "서비스 이용약관 동의 값은 필수입니다.")
                Boolean serviceTermsConsent,
        @Schema(description = "광고 수신 동의") @NotNull(message = "광고 수신 동의 값은 필수입니다.")
                Boolean adConsent) {}
