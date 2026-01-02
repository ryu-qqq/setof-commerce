package com.ryuqq.setof.application.faqcategory.manager.command;

import com.ryuqq.setof.application.faqcategory.port.out.command.FaqCategoryPersistencePort;
import com.ryuqq.setof.domain.faq.aggregate.FaqCategory;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryId;
import org.springframework.stereotype.Component;

/**
 * FAQ 카테고리 Persistence Manager
 *
 * <p>FAQ 카테고리 영속성 관련 비즈니스 로직을 처리합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class FaqCategoryPersistenceManager {

    private final FaqCategoryPersistencePort faqCategoryPersistencePort;

    public FaqCategoryPersistenceManager(FaqCategoryPersistencePort faqCategoryPersistencePort) {
        this.faqCategoryPersistencePort = faqCategoryPersistencePort;
    }

    /**
     * FAQ 카테고리 저장/수정
     *
     * @param faqCategory 저장할 FAQ 카테고리
     * @return 저장된 FAQ 카테고리 ID
     */
    public FaqCategoryId persist(FaqCategory faqCategory) {
        return faqCategoryPersistencePort.persist(faqCategory);
    }
}
