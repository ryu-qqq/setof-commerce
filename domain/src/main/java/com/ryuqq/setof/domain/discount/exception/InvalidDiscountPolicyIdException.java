package com.ryuqq.setof.domain.discount.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/** 할인 정책 ID가 유효하지 않을 때 발생하는 예외 */
public class InvalidDiscountPolicyIdException extends DomainException {

    public InvalidDiscountPolicyIdException(Long value) {
        super(
                DiscountPolicyErrorCode.INVALID_DISCOUNT_POLICY_ID,
                String.format("할인 정책 ID는 null이 아닌 양수여야 합니다. 입력값: %s", value),
                Map.of("value", value != null ? value : "null"));
    }
}
