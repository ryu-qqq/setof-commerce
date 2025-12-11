package com.ryuqq.setof.domain.category.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 카테고리명 예외
 *
 * <p>카테고리명이 null이거나 빈 값이거나 255자를 초과할 때 발생합니다.
 */
public class InvalidCategoryNameException extends DomainException {

    public InvalidCategoryNameException(String value) {
        super(
                CategoryErrorCode.INVALID_CATEGORY_NAME,
                String.format("카테고리명이 올바르지 않습니다. value: %s", value),
                Map.of("value", value != null ? value : "null"));
    }
}
