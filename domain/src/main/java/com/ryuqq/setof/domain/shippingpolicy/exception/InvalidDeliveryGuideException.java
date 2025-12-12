package com.ryuqq.setof.domain.shippingpolicy.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 배송 안내 예외
 *
 * <p>배송 안내가 최대 길이를 초과한 경우 발생합니다.
 */
public class InvalidDeliveryGuideException extends DomainException {

    public InvalidDeliveryGuideException(String value) {
        super(
                ShippingPolicyErrorCode.INVALID_DELIVERY_GUIDE,
                "배송 안내는 2000자를 초과할 수 없습니다.",
                Map.of("length", value != null ? value.length() : 0));
    }
}
