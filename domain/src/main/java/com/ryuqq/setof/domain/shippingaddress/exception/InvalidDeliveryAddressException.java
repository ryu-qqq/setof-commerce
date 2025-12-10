package com.ryuqq.setof.domain.shippingaddress.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 배송 주소 예외
 *
 * <p>배송 주소 관련 검증이 실패한 경우 발생합니다.
 */
public class InvalidDeliveryAddressException extends DomainException {

    private InvalidDeliveryAddressException(
            ShippingAddressErrorCode errorCode, String message, Map<String, Object> args) {
        super(errorCode, message, args);
    }

    public static InvalidDeliveryAddressException addressRequired() {
        return new InvalidDeliveryAddressException(
                ShippingAddressErrorCode.ADDRESS_REQUIRED, "도로명주소 또는 지번주소 중 하나는 필수입니다.", Map.of());
    }

    public static InvalidDeliveryAddressException addressTooLong(String fieldName, int maxLength) {
        return new InvalidDeliveryAddressException(
                ShippingAddressErrorCode.ADDRESS_TOO_LONG,
                String.format("%s가 최대 길이(%d자)를 초과했습니다.", fieldName, maxLength),
                Map.of("fieldName", fieldName, "maxLength", maxLength));
    }

    public static InvalidDeliveryAddressException zipCodeRequired() {
        return new InvalidDeliveryAddressException(
                ShippingAddressErrorCode.ZIP_CODE_REQUIRED, "우편번호는 필수입니다.", Map.of());
    }

    public static InvalidDeliveryAddressException invalidZipCode(String zipCode) {
        return new InvalidDeliveryAddressException(
                ShippingAddressErrorCode.INVALID_ZIP_CODE,
                String.format("우편번호 형식이 올바르지 않습니다: %s (5~10자 필요)", zipCode),
                Map.of("zipCode", zipCode != null ? zipCode : "null"));
    }
}
