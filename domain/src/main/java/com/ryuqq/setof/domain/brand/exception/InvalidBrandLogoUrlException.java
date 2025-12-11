package com.ryuqq.setof.domain.brand.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 브랜드 로고 URL 예외
 *
 * <p>브랜드 로고 URL 형식이 올바르지 않거나 500자를 초과할 때 발생합니다.
 */
public class InvalidBrandLogoUrlException extends DomainException {

    public InvalidBrandLogoUrlException(String value) {
        super(
                BrandErrorCode.INVALID_BRAND_LOGO_URL,
                String.format("브랜드 로고 URL이 올바르지 않습니다. value: %s", value),
                Map.of("value", value != null ? value : "null"));
    }
}
