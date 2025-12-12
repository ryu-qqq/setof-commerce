package com.ryuqq.setof.domain.refundpolicy.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 반품 주소 예외
 *
 * <p>반품 주소가 올바르지 않은 경우 발생합니다.
 */
public class InvalidReturnAddressException extends DomainException {

    public InvalidReturnAddressException(String reason) {
        super(
                RefundPolicyErrorCode.INVALID_RETURN_ADDRESS,
                String.format("반품 주소가 올바르지 않습니다. reason: %s", reason),
                Map.of("reason", reason));
    }
}
