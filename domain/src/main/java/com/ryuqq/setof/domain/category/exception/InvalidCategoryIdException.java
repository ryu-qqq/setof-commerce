package com.ryuqq.setof.domain.category.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 카테고리 ID 예외
 *
 * <p>카테고리 ID가 null이거나 0 이하일 때 발생합니다.
 */
public class InvalidCategoryIdException extends DomainException {

    public InvalidCategoryIdException(Long value) {
        super(
                CategoryErrorCode.INVALID_CATEGORY_ID,
                String.format("카테고리 ID는 null이 아닌 양수여야 합니다. value: %s", value),
                Map.of("value", value != null ? value : "null"));
    }
}
