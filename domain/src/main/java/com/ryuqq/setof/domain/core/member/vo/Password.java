package com.ryuqq.setof.domain.core.member.vo;

import com.ryuqq.setof.domain.core.member.exception.InvalidPasswordException;

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
 * @param value BCrypt 해시된 비밀번호 값
 */
public record Password(String value) {

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

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidPasswordException();
        }
    }
}
