package com.ryuqq.setof.domain.faq.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryCode;
import java.util.Map;

/**
 * FAQ 카테고리 Not Found 예외
 *
 * <p>FAQ 카테고리를 찾을 수 없을 때 발생합니다. HTTP 응답: 404 NOT FOUND
 */
public class FaqCategoryNotFoundException extends DomainException {

    public FaqCategoryNotFoundException(Long categoryId) {
        super(
                FaqErrorCode.FAQ_CATEGORY_NOT_FOUND,
                String.format("FAQ 카테고리를 찾을 수 없습니다: %d", categoryId),
                Map.of("categoryId", categoryId));
    }

    public FaqCategoryNotFoundException(FaqCategoryCode categoryCode) {
        super(
                FaqErrorCode.FAQ_CATEGORY_NOT_FOUND,
                String.format("FAQ 카테고리를 찾을 수 없습니다: %s", categoryCode.value()),
                Map.of("categoryCode", categoryCode.value()));
    }

    /**
     * ID로 예외 생성 (Static Factory)
     *
     * @param categoryId 카테고리 ID
     * @return FaqCategoryNotFoundException
     */
    public static FaqCategoryNotFoundException byId(Long categoryId) {
        return new FaqCategoryNotFoundException(categoryId);
    }

    /**
     * 코드로 예외 생성 (Static Factory)
     *
     * @param code 카테고리 코드
     * @return FaqCategoryNotFoundException
     */
    public static FaqCategoryNotFoundException byCode(String code) {
        return new FaqCategoryNotFoundException(FaqCategoryCode.of(code));
    }
}
