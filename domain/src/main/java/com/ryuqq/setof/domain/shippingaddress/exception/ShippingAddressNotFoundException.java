package com.ryuqq.setof.domain.shippingaddress.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;
import java.util.UUID;

/**
 * 배송지 미존재 예외
 *
 * <p>요청한 배송지 ID에 해당하는 배송지가 존재하지 않을 때 발생합니다.
 */
public class ShippingAddressNotFoundException extends DomainException {

    public ShippingAddressNotFoundException(Long shippingAddressId) {
        super(
                ShippingAddressErrorCode.SHIPPING_ADDRESS_NOT_FOUND,
                String.format("배송지를 찾을 수 없습니다. shippingAddressId: %d", shippingAddressId),
                Map.of("shippingAddressId", shippingAddressId));
    }

    public ShippingAddressNotFoundException(Long shippingAddressId, UUID memberId) {
        super(
                ShippingAddressErrorCode.SHIPPING_ADDRESS_NOT_FOUND,
                String.format(
                        "배송지를 찾을 수 없습니다. shippingAddressId: %d, memberId: %s",
                        shippingAddressId, memberId),
                Map.of("shippingAddressId", shippingAddressId, "memberId", memberId.toString()));
    }
}
