package com.ryuqq.setof.domain.shippingaddress.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 배송지 이름 예외
 *
 * <p>배송지 이름이 null, 빈 문자열, 또는 30자 초과인 경우 발생합니다.
 */
public class InvalidAddressNameException extends DomainException {

    public InvalidAddressNameException(String addressName) {
        super(
                ShippingAddressErrorCode.INVALID_ADDRESS_NAME,
                String.format("유효하지 않은 배송지 이름: %s (1~30자 필요)", addressName),
                Map.of("addressName", addressName != null ? addressName : "null"));
    }
}
