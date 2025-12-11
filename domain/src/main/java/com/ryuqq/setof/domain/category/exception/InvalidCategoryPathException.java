package com.ryuqq.setof.domain.category.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 카테고리 경로 예외
 *
 * <p>카테고리 경로가 null이거나 빈 값이거나 500자를 초과하거나 형식이 올바르지 않을 때 발생합니다.
 */
public class InvalidCategoryPathException extends DomainException {

    public InvalidCategoryPathException(String value) {
        super(
                CategoryErrorCode.INVALID_CATEGORY_PATH,
                String.format("카테고리 경로가 올바르지 않습니다. value: %s", value),
                Map.of("value", value != null ? value : "null"));
    }
}
