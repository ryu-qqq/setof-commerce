package com.ryuqq.setof.domain.shipment.vo;

import com.ryuqq.setof.domain.shipment.exception.InvalidSenderInfoException;

/**
 * 발송인 정보 Value Object
 *
 * <p>운송장의 발송인(셀러) 정보를 나타냅니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 * </ul>
 *
 * @param name 발송인 이름
 * @param phone 발송인 연락처
 * @param address 발송지 주소
 */
public record Sender(String name, String phone, String address) {

    private static final int NAME_MAX_LENGTH = 50;
    private static final int PHONE_MAX_LENGTH = 20;
    private static final int ADDRESS_MAX_LENGTH = 200;

    /** Compact Constructor - 검증 로직 */
    public Sender {
        validate(name, phone, address);
    }

    /**
     * Static Factory Method
     *
     * @param name 발송인 이름
     * @param phone 발송인 연락처
     * @param address 발송지 주소
     * @return Sender 인스턴스
     * @throws InvalidSenderInfoException 정보가 유효하지 않은 경우
     */
    public static Sender of(String name, String phone, String address) {
        return new Sender(name, phone, address);
    }

    private static void validate(String name, String phone, String address) {
        if (name == null || name.isBlank()) {
            throw new InvalidSenderInfoException("name is required");
        }
        if (name.length() > NAME_MAX_LENGTH) {
            throw new InvalidSenderInfoException("name exceeds max length");
        }
        if (phone == null || phone.isBlank()) {
            throw new InvalidSenderInfoException("phone is required");
        }
        if (phone.length() > PHONE_MAX_LENGTH) {
            throw new InvalidSenderInfoException("phone exceeds max length");
        }
        if (address != null && address.length() > ADDRESS_MAX_LENGTH) {
            throw new InvalidSenderInfoException("address exceeds max length");
        }
    }

    /**
     * 주소 존재 여부 확인
     *
     * @return 주소가 있으면 true
     */
    public boolean hasAddress() {
        return address != null && !address.isBlank();
    }
}
