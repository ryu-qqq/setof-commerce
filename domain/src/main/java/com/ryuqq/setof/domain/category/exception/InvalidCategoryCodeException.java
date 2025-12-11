package com.ryuqq.setof.domain.category.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 카테고리 코드 예외
 *
 * <p>카테고리 코드가 null이거나 빈 값이거나 100자를 초과할 때 발생합니다.
 */
public class InvalidCategoryCodeException extends DomainException {

    public InvalidCategoryCodeException(String value) {
        super(
                CategoryErrorCode.INVALID_CATEGORY_CODE,
                String.format("카테고리 코드가 올바르지 않습니다. value: %s", value),
                Map.of("value", value != null ? value : "null"));
    }
}
