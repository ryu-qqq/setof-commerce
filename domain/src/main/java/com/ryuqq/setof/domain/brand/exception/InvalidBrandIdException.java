package com.ryuqq.setof.domain.brand.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 브랜드 ID 예외
 *
 * <p>브랜드 ID가 null이거나 0 이하일 때 발생합니다.
 */
public class InvalidBrandIdException extends DomainException {

    public InvalidBrandIdException(Long value) {
        super(
                BrandErrorCode.INVALID_BRAND_ID,
                String.format("브랜드 ID는 null이 아닌 양수여야 합니다. value: %s", value),
                Map.of("value", value != null ? value : "null"));
    }
}
