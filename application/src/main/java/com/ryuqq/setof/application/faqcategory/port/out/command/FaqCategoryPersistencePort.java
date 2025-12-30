package com.ryuqq.setof.application.faqcategory.port.out.command;

import com.ryuqq.setof.domain.faq.aggregate.FaqCategory;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryId;

/**
 * FAQ 카테고리 영속성 Outbound Port (Command)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface FaqCategoryPersistencePort {

    /**
     * FAQ 카테고리 저장 (신규 생성 및 수정 통합)
     *
     * @param faqCategory FAQ 카테고리 Aggregate
     * @return 저장된 FAQ 카테고리 ID
     */
    FaqCategoryId persist(FaqCategory faqCategory);
}
