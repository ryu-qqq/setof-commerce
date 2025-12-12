package com.ryuqq.setof.domain.seller.vo;

import com.ryuqq.setof.domain.seller.exception.InvalidRegistrationNumberException;
import java.util.regex.Pattern;

/**
 * 사업자등록번호 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>형식: 000-00-00000 또는 0000000000
 * </ul>
 *
 * @param value 사업자등록번호 값
 */
public record RegistrationNumber(String value) {

    private static final Pattern FORMAT_PATTERN =
            Pattern.compile("^\\d{3}-\\d{2}-\\d{5}$|^\\d{10}$");

    /** Compact Constructor - 검증 로직 */
    public RegistrationNumber {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 사업자등록번호 값
     * @return RegistrationNumber 인스턴스
     * @throws InvalidRegistrationNumberException value가 null이거나 형식이 올바르지 않은 경우
     */
    public static RegistrationNumber of(String value) {
        return new RegistrationNumber(value);
    }

    /**
     * 정규화된 값 반환 (하이픈 제거)
     *
     * @return 하이픈이 제거된 10자리 사업자등록번호
     */
    public String normalizedValue() {
        return value.replaceAll("-", "");
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidRegistrationNumberException(value, "사업자등록번호는 필수입니다.");
        }

        if (!FORMAT_PATTERN.matcher(value).matches()) {
            throw new InvalidRegistrationNumberException(
                    value, "사업자등록번호 형식이 올바르지 않습니다. (000-00-00000 또는 10자리 숫자)");
        }
    }
}
