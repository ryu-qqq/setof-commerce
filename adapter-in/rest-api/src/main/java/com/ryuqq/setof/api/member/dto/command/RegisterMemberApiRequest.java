package com.ryuqq.setof.api.member.dto.command;

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
 *   <li>phoneNumber: 010으로 시작하는 11자리
 *   <li>password: 8자 이상, 영문+숫자+특수문자 포함
 *   <li>name: 2~50자
 *   <li>consents: 필수 동의 항목
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public record RegisterMemberApiRequest(
        @NotBlank(message = "핸드폰 번호는 필수입니다.")
                @Pattern(regexp = "^010\\d{8}$", message = "핸드폰 번호 형식이 올바르지 않습니다.")
                String phoneNumber,
        String email,
        @NotBlank(message = "비밀번호는 필수입니다.")
                @Pattern(
                        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
                        message = "비밀번호는 8자 이상, 영문, 숫자, 특수문자를 포함해야 합니다.")
                String password,
        @NotBlank(message = "이름은 필수입니다.") @Size(min = 2, max = 50, message = "이름은 2~50자여야 합니다.")
                String name,
        LocalDate dateOfBirth,
        String gender,
        @NotNull(message = "개인정보 수집 동의는 필수입니다.") Boolean privacyConsent,
        @NotNull(message = "서비스 이용약관 동의는 필수입니다.") Boolean serviceTermsConsent,
        @NotNull(message = "광고 수신 동의 여부는 필수입니다.") Boolean adConsent) {}
