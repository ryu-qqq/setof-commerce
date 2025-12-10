package com.ryuqq.setof.domain.shippingaddress.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;
import java.util.UUID;

/**
 * 배송지 소유권 없음 예외
 *
 * <p>다른 회원의 배송지에 접근하려는 경우 발생합니다.
 */
public class ShippingAddressNotOwnerException extends DomainException {

    public ShippingAddressNotOwnerException(Long shippingAddressId, UUID requestMemberId) {
        super(
                ShippingAddressErrorCode.NOT_OWNER,
                String.format(
                        "해당 배송지에 대한 권한이 없습니다. shippingAddressId: %d, memberId: %s",
                        shippingAddressId, requestMemberId),
                Map.of(
                        "shippingAddressId",
                        shippingAddressId,
                        "requestMemberId",
                        requestMemberId.toString()));
    }
}
