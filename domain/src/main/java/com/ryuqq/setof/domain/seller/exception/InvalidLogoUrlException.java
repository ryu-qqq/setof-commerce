package com.ryuqq.setof.domain.seller.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 로고 URL 예외
 *
 * <p>로고 URL 형식이 올바르지 않은 경우 발생합니다.
 */
public class InvalidLogoUrlException extends DomainException {

    public InvalidLogoUrlException(String value, String reason) {
        super(
                SellerErrorCode.INVALID_LOGO_URL,
                String.format("로고 URL이 올바르지 않습니다. value: %s, reason: %s", value, reason),
                Map.of("value", value != null ? value : "null", "reason", reason));
    }
}
