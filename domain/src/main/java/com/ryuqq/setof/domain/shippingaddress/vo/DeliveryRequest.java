package com.ryuqq.setof.domain.shippingaddress.vo;

/**
 * 배송 요청사항 Value Object.
 *
 * <p>nullable 허용. 빈 문자열은 null로 정규화됩니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record DeliveryRequest(String value) {

    private static final int MAX_LENGTH = 500;

    public DeliveryRequest {
        if (value != null) {
            value = value.trim();
            if (value.isBlank()) {
                value = null;
            } else if (value.length() > MAX_LENGTH) {
                throw new IllegalArgumentException(
                        String.format("배송 요청사항은 %d자를 초과할 수 없습니다: %d자", MAX_LENGTH, value.length()));
            }
        }
    }

    public static DeliveryRequest of(String value) {
        return new DeliveryRequest(value);
    }

    public static DeliveryRequest empty() {
        return new DeliveryRequest(null);
    }
}
