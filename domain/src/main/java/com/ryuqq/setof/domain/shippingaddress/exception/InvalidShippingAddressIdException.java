package com.ryuqq.setof.domain.shippingaddress.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 배송지 ID 예외
 *
 * <p>배송지 ID가 null이거나 0 이하인 경우 발생합니다.
 */
public class InvalidShippingAddressIdException extends DomainException {

    public InvalidShippingAddressIdException(Long shippingAddressId) {
        super(
                ShippingAddressErrorCode.INVALID_SHIPPING_ADDRESS_ID,
                String.format("유효하지 않은 배송지 ID: %s", shippingAddressId),
                Map.of(
                        "shippingAddressId",
                        shippingAddressId != null ? shippingAddressId : "null"));
    }
}
