package com.ryuqq.setof.domain.seller.vo;

import com.ryuqq.setof.domain.seller.exception.InvalidCsLandlinePhoneException;
import java.util.regex.Pattern;

/**
 * CS 유선전화 번호 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>한국 유선전화 형식 검증 (지역번호 포함)
 *   <li>nullable 허용 - 선택적 필드
 * </ul>
 *
 * @param value CS 유선전화 번호 값 (null 허용)
 */
public record CsLandlinePhone(String value) {

    private static final Pattern LANDLINE_PATTERN =
            Pattern.compile("^0(2|3[1-3]|4[1-4]|5[1-5]|6[1-4])-?\\d{3,4}-?\\d{4}$");

    /** Compact Constructor - 검증 로직 */
    public CsLandlinePhone {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value CS 유선전화 번호 값 (null 허용)
     * @return CsLandlinePhone 인스턴스
     * @throws InvalidCsLandlinePhoneException value가 빈 문자열이거나 잘못된 형식인 경우
     */
    public static CsLandlinePhone of(String value) {
        return new CsLandlinePhone(value);
    }

    /**
     * 값 존재 여부 확인
     *
     * @return 값이 있으면 true, null이면 false
     */
    public boolean hasValue() {
        return value != null;
    }

    /**
     * 정규화된 값 반환 (하이픈 제거)
     *
     * @return 하이픈이 제거된 유선전화 번호, null인 경우 null 반환
     */
    public String normalizedValue() {
        return value != null ? value.replaceAll("-", "") : null;
    }

    private static void validate(String value) {
        if (value == null) {
            return;
        }

        if (value.isBlank()) {
            throw new InvalidCsLandlinePhoneException(value, "CS 유선전화 번호는 빈 문자열일 수 없습니다.");
        }

        if (!LANDLINE_PATTERN.matcher(value).matches()) {
            throw new InvalidCsLandlinePhoneException(
                    value, "올바른 유선전화 번호 형식이어야 합니다. (예: 02-1234-5678)");
        }
    }
}
