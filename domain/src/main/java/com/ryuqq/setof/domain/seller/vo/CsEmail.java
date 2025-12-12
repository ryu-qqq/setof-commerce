package com.ryuqq.setof.domain.seller.vo;

import com.ryuqq.setof.domain.seller.exception.InvalidCsEmailException;
import java.util.regex.Pattern;

/**
 * CS 이메일 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>RFC 5322 형식 검증
 *   <li>nullable 허용 - 선택적 필드
 * </ul>
 *
 * @param value CS 이메일 값 (null 허용)
 */
public record CsEmail(String value) {

    private static final int MAX_LENGTH = 100;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9]([a-zA-Z0-9-]*[a-zA-Z0-9])?(\\.[a-zA-Z0-9]([a-zA-Z0-9-]*[a-zA-Z0-9])?)+$");

    /** Compact Constructor - 검증 로직 */
    public CsEmail {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value CS 이메일 값 (null 허용)
     * @return CsEmail 인스턴스
     * @throws InvalidCsEmailException value가 빈 문자열이거나 잘못된 형식인 경우
     */
    public static CsEmail of(String value) {
        return new CsEmail(value);
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
        if (value == null) {
            return;
        }

        if (value.isBlank()) {
            throw new InvalidCsEmailException(value, "CS 이메일은 빈 문자열일 수 없습니다.");
        }

        if (value.length() > MAX_LENGTH) {
            throw new InvalidCsEmailException(value, "CS 이메일은 100자를 초과할 수 없습니다.");
        }

        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new InvalidCsEmailException(value, "RFC 5322 형식이어야 합니다.");
        }
    }
}
