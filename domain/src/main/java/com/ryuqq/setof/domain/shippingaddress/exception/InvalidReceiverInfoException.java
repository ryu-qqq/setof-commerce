package com.ryuqq.setof.domain.shippingaddress.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 수령인 정보 예외
 *
 * <p>수령인 이름 또는 연락처가 유효하지 않은 경우 발생합니다.
 */
public class InvalidReceiverInfoException extends DomainException {

    private InvalidReceiverInfoException(ShippingAddressErrorCode errorCode, String message, Map<String, Object> args) {
        super(errorCode, message, args);
    }

    public static InvalidReceiverInfoException invalidName(String name) {
        return new InvalidReceiverInfoException(
                ShippingAddressErrorCode.INVALID_RECEIVER_NAME,
                String.format("유효하지 않은 수령인 이름: %s (1~20자 필요)", name),
                Map.of("receiverName", name != null ? name : "null"));
    }

    public static InvalidReceiverInfoException invalidPhone(String phone) {
        return new InvalidReceiverInfoException(
                ShippingAddressErrorCode.INVALID_RECEIVER_PHONE,
                String.format("유효하지 않은 수령인 연락처: %s (10~15자리 숫자 필요)", phone),
                Map.of("receiverPhone", phone != null ? phone : "null"));
    }
}
