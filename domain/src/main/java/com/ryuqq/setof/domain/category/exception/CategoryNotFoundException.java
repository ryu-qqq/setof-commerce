package com.ryuqq.setof.domain.category.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 카테고리 미존재 예외
 *
 * <p>요청한 카테고리 ID 또는 코드에 해당하는 카테고리가 존재하지 않을 때 발생합니다.
 */
public class CategoryNotFoundException extends DomainException {

    public CategoryNotFoundException(Long categoryId) {
        super(
                CategoryErrorCode.CATEGORY_NOT_FOUND,
                String.format("카테고리를 찾을 수 없습니다. categoryId: %d", categoryId),
                Map.of("categoryId", categoryId));
    }

    public CategoryNotFoundException(String categoryCode) {
        super(
                CategoryErrorCode.CATEGORY_NOT_FOUND,
                String.format("카테고리를 찾을 수 없습니다. categoryCode: %s", categoryCode),
                Map.of("categoryCode", categoryCode));
    }
}
