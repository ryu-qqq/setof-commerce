package com.ryuqq.setof.domain.shippingaddress.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;
import java.util.UUID;

/**
 * 배송지 개수 초과 예외
 *
 * <p>회원당 최대 배송지 개수(5개)를 초과하여 등록하려는 경우 발생합니다.
 */
public class ShippingAddressLimitExceededException extends DomainException {

    private static final int MAX_SHIPPING_ADDRESS_COUNT = 5;

    public ShippingAddressLimitExceededException(UUID memberId, int currentCount) {
        super(
                ShippingAddressErrorCode.SHIPPING_ADDRESS_LIMIT_EXCEEDED,
                String.format(
                        "배송지는 최대 %d개까지만 등록할 수 있습니다. 현재: %d개",
                        MAX_SHIPPING_ADDRESS_COUNT, currentCount),
                Map.of(
                        "memberId",
                        memberId.toString(),
                        "currentCount",
                        currentCount,
                        "maxCount",
                        MAX_SHIPPING_ADDRESS_COUNT));
    }
}
