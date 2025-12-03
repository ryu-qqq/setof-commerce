package com.ryuqq.setof.domain.core.member.vo;

import com.ryuqq.setof.domain.core.member.exception.InvalidPhoneNumberException;
import java.util.regex.Pattern;

/**
 * 핸드폰 번호 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>010[0-9]{8} 정규식 검증
 *   <li>Private 생성자 + Static Factory - 외부 직접 생성 금지
 * </ul>
 *
 * @param value 핸드폰 번호 값 (01012345678 형식)
 */
public record PhoneNumber(String value) {

    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^010[0-9]{8}$");

    /** Compact Constructor - 검증 로직 */
    public PhoneNumber {
        validate(value);
    }

    /**
     * Static Factory Method - 신규 생성용
     *
     * @param value 핸드폰 번호 값
     * @return PhoneNumber 인스턴스
     * @throws InvalidPhoneNumberException value가 null이거나 잘못된 형식인 경우
     */
    public static PhoneNumber of(String value) {
        return new PhoneNumber(value);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidPhoneNumberException();
        }

        if (!PHONE_NUMBER_PATTERN.matcher(value).matches()) {
            throw new InvalidPhoneNumberException(value, "010으로 시작하는 11자리 숫자여야 합니다.");
        }
    }
}
