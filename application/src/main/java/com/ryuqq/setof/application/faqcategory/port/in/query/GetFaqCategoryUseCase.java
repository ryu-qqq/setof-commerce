package com.ryuqq.setof.application.faqcategory.port.in.query;

import com.ryuqq.setof.application.faqcategory.dto.response.FaqCategoryResponse;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryId;

/**
 * FAQ 카테고리 단건 조회 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetFaqCategoryUseCase {

    /**
     * FAQ 카테고리 ID로 조회
     *
     * @param categoryId FAQ 카테고리 ID
     * @return FAQ 카테고리 응답
     */
    FaqCategoryResponse execute(FaqCategoryId categoryId);
}
