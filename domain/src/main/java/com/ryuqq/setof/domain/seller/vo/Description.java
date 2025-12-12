package com.ryuqq.setof.domain.seller.vo;

import com.ryuqq.setof.domain.seller.exception.InvalidDescriptionException;

/**
 * 셀러 설명 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>nullable 허용 - 선택적 필드
 * </ul>
 *
 * @param value 셀러 설명 값 (null 허용)
 */
public record Description(String value) {

    private static final int MAX_LENGTH = 2000;

    /** Compact Constructor - 검증 로직 */
    public Description {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 셀러 설명 값 (null 허용)
     * @return Description 인스턴스
     * @throws InvalidDescriptionException value가 최대 길이를 초과한 경우
     */
    public static Description of(String value) {
        return new Description(value);
    }

    /**
     * 값 존재 여부 확인
     *
     * @return 값이 있으면 true, null이면 false
     */
    public boolean hasValue() {
        return value != null && !value.isBlank();
    }

    private static void validate(String value) {
        if (value == null) {
            return;
        }

        if (value.length() > MAX_LENGTH) {
            throw new InvalidDescriptionException(value);
        }
    }
}
