package com.ryuqq.setof.domain.discount.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/** 할인 정책을 찾을 수 없을 때 발생하는 예외 */
public class DiscountPolicyNotFoundException extends DomainException {

    public DiscountPolicyNotFoundException(Long discountPolicyId) {
        super(
                DiscountPolicyErrorCode.DISCOUNT_POLICY_NOT_FOUND,
                String.format("할인 정책을 찾을 수 없습니다. ID: %d", discountPolicyId),
                Map.of("discountPolicyId", discountPolicyId));
    }
}
