package com.ryuqq.setof.domain.core.member.vo;

import com.ryuqq.setof.domain.core.member.exception.InvalidEmailException;
import java.util.regex.Pattern;

/**
 * 이메일 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>RFC 5322 형식 검증
 *   <li>nullable 허용 - 선택적 필드
 *   <li>Private 생성자 + Static Factory - 외부 직접 생성 금지
 * </ul>
 *
 * @param value 이메일 값 (null 허용)
 */
public record Email(String value) {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9]([a-zA-Z0-9-]*[a-zA-Z0-9])?(\\.[a-zA-Z0-9]([a-zA-Z0-9-]*[a-zA-Z0-9])?)+$");

    /** Compact Constructor - 검증 로직 */
    public Email {
        validate(value);
    }

    /**
     * Static Factory Method - 신규 생성용
     *
     * @param value 이메일 값 (null 허용)
     * @return Email 인스턴스
     * @throws InvalidEmailException value가 빈 문자열이거나 잘못된 형식인 경우
     */
    public static Email of(String value) {
        return new Email(value);
    }

    /**
     * 값 존재 여부 확인
     *
     * @return 값이 있으면 true, null이면 false
     */
    public boolean hasValue() {
        return value != null;
    }

    private static void validate(String value) {
        // null은 허용 (nullable 필드)
        if (value == null) {
            return;
        }

        // 빈 문자열 또는 공백만 있는 경우 예외
        if (value.isBlank()) {
            throw new InvalidEmailException();
        }

        // RFC 5322 형식 검증
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new InvalidEmailException(value, "RFC 5322 형식이어야 합니다.");
        }
    }
}
