package com.ryuqq.setof.domain.discount.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.time.Instant;
import java.util.Map;

/** 유효 기간이 유효하지 않을 때 발생하는 예외 */
public class InvalidValidPeriodException extends DomainException {

    public InvalidValidPeriodException(Instant startAt, Instant endAt, String reason) {
        super(
                DiscountPolicyErrorCode.INVALID_VALID_PERIOD,
                String.format("유효 기간이 올바르지 않습니다. %s 시작: %s, 종료: %s", reason, startAt, endAt),
                Map.of(
                        "startAt", startAt != null ? startAt.toString() : "null",
                        "endAt", endAt != null ? endAt.toString() : "null",
                        "reason", reason));
    }
}
