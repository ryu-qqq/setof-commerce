package com.ryuqq.setof.domain.category.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 카테고리 깊이 예외
 *
 * <p>카테고리 깊이가 음수일 때 발생합니다.
 */
public class InvalidCategoryDepthException extends DomainException {

    public InvalidCategoryDepthException(Integer value) {
        super(
                CategoryErrorCode.INVALID_CATEGORY_DEPTH,
                String.format("카테고리 깊이가 올바르지 않습니다. value: %s", value),
                Map.of("value", value != null ? value : "null"));
    }
}
