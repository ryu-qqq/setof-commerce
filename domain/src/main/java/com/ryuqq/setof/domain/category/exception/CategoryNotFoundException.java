package com.ryuqq.setof.domain.category.exception;

/**
 * 카테고리를 찾을 수 없는 경우 예외.
 *
 * <p>요청한 ID에 해당하는 카테고리가 존재하지 않을 때 발생합니다.
 */
public class CategoryNotFoundException extends CategoryException {

    private static final CategoryErrorCode ERROR_CODE = CategoryErrorCode.CATEGORY_NOT_FOUND;

    public CategoryNotFoundException() {
        super(ERROR_CODE);
    }

    public CategoryNotFoundException(Long id) {
        super(ERROR_CODE, String.format("ID가 %d인 카테고리를 찾을 수 없습니다", id));
    }
}
