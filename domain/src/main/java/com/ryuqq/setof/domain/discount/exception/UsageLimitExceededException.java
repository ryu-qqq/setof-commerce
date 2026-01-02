package com.ryuqq.setof.domain.discount.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/** 사용 횟수 제한을 초과했을 때 발생하는 예외 */
public class UsageLimitExceededException extends DomainException {

    public UsageLimitExceededException(Long discountPolicyId, int currentUsage, int limit) {
        super(
                DiscountPolicyErrorCode.USAGE_LIMIT_EXCEEDED,
                String.format(
                        "사용 횟수 제한을 초과했습니다. 현재: %d, 제한: %d, 정책 ID: %d",
                        currentUsage, limit, discountPolicyId),
                Map.of(
                        "discountPolicyId", discountPolicyId,
                        "currentUsage", currentUsage,
                        "limit", limit));
    }
}
