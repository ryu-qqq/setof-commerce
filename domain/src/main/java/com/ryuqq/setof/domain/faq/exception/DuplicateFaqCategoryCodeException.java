package com.ryuqq.setof.domain.faq.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryCode;
import java.util.Map;

/**
 * FAQ 카테고리 코드 중복 예외
 *
 * <p>이미 존재하는 카테고리 코드로 생성하려 할 때 발생합니다. HTTP 응답: 409 CONFLICT
 */
public class DuplicateFaqCategoryCodeException extends DomainException {

    public DuplicateFaqCategoryCodeException(FaqCategoryCode code) {
        super(
                FaqErrorCode.DUPLICATE_FAQ_CATEGORY_CODE,
                String.format("이미 존재하는 FAQ 카테고리 코드입니다: %s", code.value()),
                Map.of("categoryCode", code.value()));
    }

    public DuplicateFaqCategoryCodeException(String code) {
        super(
                FaqErrorCode.DUPLICATE_FAQ_CATEGORY_CODE,
                String.format("이미 존재하는 FAQ 카테고리 코드입니다: %s", code),
                Map.of("categoryCode", code));
    }

    /**
     * 코드로 예외 생성 (Static Factory)
     *
     * @param code 카테고리 코드
     * @return DuplicateFaqCategoryCodeException
     */
    public static DuplicateFaqCategoryCodeException of(String code) {
        return new DuplicateFaqCategoryCodeException(code);
    }
}
