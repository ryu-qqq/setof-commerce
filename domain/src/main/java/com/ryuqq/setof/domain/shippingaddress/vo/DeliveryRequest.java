package com.ryuqq.setof.domain.shippingaddress.vo;

import com.ryuqq.setof.domain.shippingaddress.exception.InvalidDeliveryRequestException;

/**
 * 배송 요청사항 Value Object
 *
 * <p>배송 시 요청사항을 관리합니다.
 * 예: "문 앞에 놔주세요", "부재 시 경비실에 맡겨주세요"
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>최대 200자, nullable 허용
 * </ul>
 *
 * @param value 배송 요청사항 (nullable)
 */
public record DeliveryRequest(String value) {

    private static final int MAX_LENGTH = 200;

    /** Compact Constructor - 검증 로직 */
    public DeliveryRequest {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 배송 요청사항
     * @return DeliveryRequest 인스턴스
     * @throws InvalidDeliveryRequestException 값이 200자를 초과하는 경우
     */
    public static DeliveryRequest of(String value) {
        return new DeliveryRequest(value);
    }

    /**
     * 빈 배송 요청사항 생성
     *
     * @return 빈 DeliveryRequest 인스턴스
     */
    public static DeliveryRequest empty() {
        return new DeliveryRequest(null);
    }

    private static void validate(String value) {
        if (value != null && value.length() > MAX_LENGTH) {
            throw new InvalidDeliveryRequestException(value);
        }
    }

    /**
     * 값이 비어있는지 확인
     *
     * @return 값이 null이거나 빈 문자열이면 true
     */
    public boolean isEmpty() {
        return value == null || value.isBlank();
    }

    /**
     * 값이 존재하는지 확인
     *
     * @return 값이 존재하면 true
     */
    public boolean hasValue() {
        return !isEmpty();
    }
}
