package com.ryuqq.setof.domain.refundpolicy.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 반품 안내 예외
 *
 * <p>반품 안내가 최대 길이를 초과한 경우 발생합니다.
 */
public class InvalidRefundGuideException extends DomainException {

    public InvalidRefundGuideException(String value) {
        super(
                RefundPolicyErrorCode.INVALID_REFUND_GUIDE,
                "반품 안내는 2000자를 초과할 수 없습니다.",
                Map.of("length", value != null ? value.length() : 0));
    }
}
