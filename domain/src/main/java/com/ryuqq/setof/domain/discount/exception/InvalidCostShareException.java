package com.ryuqq.setof.domain.discount.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.math.BigDecimal;
import java.util.Map;

/** 비용 분담 비율이 유효하지 않을 때 발생하는 예외 */
public class InvalidCostShareException extends DomainException {

    public InvalidCostShareException(
            BigDecimal platformRatio, BigDecimal sellerRatio, String reason) {
        super(
                DiscountPolicyErrorCode.INVALID_COST_SHARE,
                String.format(
                        "비용 분담 비율이 올바르지 않습니다. %s 플랫폼: %s%%, 셀러: %s%%",
                        reason, platformRatio, sellerRatio),
                Map.of(
                        "platformRatio", platformRatio != null ? platformRatio.toString() : "null",
                        "sellerRatio", sellerRatio != null ? sellerRatio.toString() : "null",
                        "reason", reason));
    }
}
