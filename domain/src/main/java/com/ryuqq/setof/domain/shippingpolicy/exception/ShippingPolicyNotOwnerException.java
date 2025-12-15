package com.ryuqq.setof.domain.shippingpolicy.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 배송 정책 소유권 없음 예외
 *
 * <p>다른 셀러의 배송 정책에 접근하려는 경우 발생합니다.
 */
public class ShippingPolicyNotOwnerException extends DomainException {

    public ShippingPolicyNotOwnerException(Long shippingPolicyId, Long requestSellerId) {
        super(
                ShippingPolicyErrorCode.NOT_OWNER,
                String.format(
                        "해당 배송 정책에 대한 권한이 없습니다. shippingPolicyId: %d, sellerId: %d",
                        shippingPolicyId, requestSellerId),
                Map.of("shippingPolicyId", shippingPolicyId, "requestSellerId", requestSellerId));
    }
}
