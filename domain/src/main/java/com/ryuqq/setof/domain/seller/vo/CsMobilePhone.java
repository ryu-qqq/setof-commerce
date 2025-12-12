package com.ryuqq.setof.domain.seller.vo;

import com.ryuqq.setof.domain.seller.exception.InvalidCsMobilePhoneException;
import java.util.regex.Pattern;

/**
 * CS 휴대폰 번호 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>한국 휴대폰 형식 검증 (010, 011, 016, 017, 018, 019)
 *   <li>nullable 허용 - 선택적 필드
 * </ul>
 *
 * @param value CS 휴대폰 번호 값 (null 허용)
 */
public record CsMobilePhone(String value) {

    private static final Pattern MOBILE_PATTERN = Pattern.compile("^01[016789]-?\\d{3,4}-?\\d{4}$");

    /** Compact Constructor - 검증 로직 */
    public CsMobilePhone {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value CS 휴대폰 번호 값 (null 허용)
     * @return CsMobilePhone 인스턴스
     * @throws InvalidCsMobilePhoneException value가 빈 문자열이거나 잘못된 형식인 경우
     */
    public static CsMobilePhone of(String value) {
        return new CsMobilePhone(value);
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
     * @return 하이픈이 제거된 휴대폰 번호, null인 경우 null 반환
     */
    public String normalizedValue() {
        return value != null ? value.replaceAll("-", "") : null;
    }

    private static void validate(String value) {
        if (value == null) {
            return;
        }

        if (value.isBlank()) {
            throw new InvalidCsMobilePhoneException(value, "CS 휴대폰 번호는 빈 문자열일 수 없습니다.");
        }

        if (!MOBILE_PATTERN.matcher(value).matches()) {
            throw new InvalidCsMobilePhoneException(
                    value, "올바른 휴대폰 번호 형식이어야 합니다. (예: 010-1234-5678)");
        }
    }
}
