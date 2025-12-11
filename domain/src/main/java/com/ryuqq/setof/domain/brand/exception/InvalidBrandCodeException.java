package com.ryuqq.setof.domain.brand.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 브랜드 코드 예외
 *
 * <p>브랜드 코드가 null이거나 빈 값이거나 100자를 초과할 때 발생합니다.
 */
public class InvalidBrandCodeException extends DomainException {

    public InvalidBrandCodeException(String value) {
        super(
                BrandErrorCode.INVALID_BRAND_CODE,
                String.format("브랜드 코드가 올바르지 않습니다. value: %s", value),
                Map.of("value", value != null ? value : "null"));
    }
}
