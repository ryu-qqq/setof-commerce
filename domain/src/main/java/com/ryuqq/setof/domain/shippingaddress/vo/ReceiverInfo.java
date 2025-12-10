package com.ryuqq.setof.domain.shippingaddress.vo;

import com.ryuqq.setof.domain.shippingaddress.exception.InvalidReceiverInfoException;

/**
 * 수령인 정보 Value Object (Composite VO)
 *
 * <p>배송 수령인의 이름과 연락처를 함께 관리합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>수령인 이름: 1~20자
 *   <li>연락처: 10~15자, 숫자만 허용
 * </ul>
 *
 * @param name 수령인 이름
 * @param phone 수령인 연락처
 */
public record ReceiverInfo(String name, String phone) {

    private static final int NAME_MAX_LENGTH = 20;
    private static final int PHONE_MIN_LENGTH = 10;
    private static final int PHONE_MAX_LENGTH = 15;

    /** Compact Constructor - 검증 로직 */
    public ReceiverInfo {
        validateName(name);
        validatePhone(phone);
    }

    /**
     * Static Factory Method
     *
     * @param name 수령인 이름
     * @param phone 수령인 연락처
     * @return ReceiverInfo 인스턴스
     * @throws InvalidReceiverInfoException 값이 유효하지 않은 경우
     */
    public static ReceiverInfo of(String name, String phone) {
        return new ReceiverInfo(name, phone);
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw InvalidReceiverInfoException.invalidName(name);
        }
        if (name.length() > NAME_MAX_LENGTH) {
            throw InvalidReceiverInfoException.invalidName(name);
        }
    }

    private static void validatePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            throw InvalidReceiverInfoException.invalidPhone(phone);
        }
        String digits = phone.replaceAll("[^0-9]", "");
        if (digits.length() < PHONE_MIN_LENGTH || digits.length() > PHONE_MAX_LENGTH) {
            throw InvalidReceiverInfoException.invalidPhone(phone);
        }
    }

    /**
     * 정규화된 전화번호 반환 (숫자만)
     *
     * @return 숫자만 포함된 전화번호
     */
    public String normalizedPhone() {
        return phone.replaceAll("[^0-9]", "");
    }
}
