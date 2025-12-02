package com.ryuqq.setof.domain.core.member.vo;

import com.ryuqq.setof.domain.core.member.exception.InvalidPasswordException;
import com.ryuqq.setof.domain.core.member.exception.PasswordPolicyViolationException;

import java.util.regex.Pattern;

/**
 * 비밀번호 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:</p>
 * <ul>
 *     <li>Lombok 금지 - Pure Java Record 사용</li>
 *     <li>불변성 보장 - Java Record 특성</li>
 *     <li>BCrypt 해시값 저장 (해시화는 Application Layer 책임)</li>
 *     <li>NotBlank 검증</li>
 *     <li>Private 생성자 + Static Factory - 외부 직접 생성 금지</li>
 * </ul>
 *
 * <p>주의: 이 VO는 이미 해시된 비밀번호를 저장합니다.
 * 원본 비밀번호의 해시화는 Application Layer에서 수행됩니다.</p>
 *
 * <p>비밀번호 정책 (회원가입 시 Application Layer에서 검증):</p>
 * <ul>
 *     <li>8자 이상</li>
 *     <li>영문 대문자 1개 이상</li>
 *     <li>영문 소문자 1개 이상</li>
 *     <li>숫자 1개 이상</li>
 *     <li>특수문자 1개 이상</li>
 * </ul>
 *
 * @param value BCrypt 해시된 비밀번호 값
 */
public record Password(String value) {

    private static final int MIN_LENGTH = 8;
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%^&*(),.?\":{}|<>\\[\\]\\-_=+;'`~\\\\/]");

    /**
     * Compact Constructor - 검증 로직
     */
    public Password {
        validate(value);
    }

    /**
     * Static Factory Method - 신규 생성용
     *
     * @param value 해시된 비밀번호 값
     * @return Password 인스턴스
     * @throws InvalidPasswordException value가 null이거나 빈 문자열인 경우
     */
    public static Password of(String value) {
        return new Password(value);
    }

    /**
     * 비밀번호 정책 검증 (회원가입 시 사용)
     *
     * <p>Application Layer에서 원본 비밀번호의 정책 준수 여부를 검증할 때 사용합니다.
     * 해시화 전 원본 비밀번호에 대해 호출해야 합니다.</p>
     *
     * @param rawPassword 원본 비밀번호
     * @throws PasswordPolicyViolationException 정책 위반 시
     */
    public static void validatePolicy(String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new PasswordPolicyViolationException();
        }
        if (rawPassword.length() < MIN_LENGTH) {
            throw new PasswordPolicyViolationException();
        }
        if (!UPPERCASE_PATTERN.matcher(rawPassword).find()) {
            throw new PasswordPolicyViolationException();
        }
        if (!LOWERCASE_PATTERN.matcher(rawPassword).find()) {
            throw new PasswordPolicyViolationException();
        }
        if (!DIGIT_PATTERN.matcher(rawPassword).find()) {
            throw new PasswordPolicyViolationException();
        }
        if (!SPECIAL_CHAR_PATTERN.matcher(rawPassword).find()) {
            throw new PasswordPolicyViolationException();
        }
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidPasswordException();
        }
    }
}
