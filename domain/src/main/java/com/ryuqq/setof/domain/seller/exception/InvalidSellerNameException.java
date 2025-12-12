package com.ryuqq.setof.domain.seller.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 셀러 이름 예외
 *
 * <p>셀러 이름이 null이거나 빈 문자열이거나 최대 길이를 초과한 경우 발생합니다.
 */
public class InvalidSellerNameException extends DomainException {

    public InvalidSellerNameException(String value, String reason) {
        super(
                SellerErrorCode.INVALID_SELLER_NAME,
                String.format("셀러 이름이 올바르지 않습니다. value: %s, reason: %s", value, reason),
                Map.of("value", value != null ? value : "null", "reason", reason));
    }
}
