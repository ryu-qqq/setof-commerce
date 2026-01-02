package com.ryuqq.setof.application.faqcategory.service.command;

import com.ryuqq.setof.application.faqcategory.dto.command.DeleteFaqCategoryCommand;
import com.ryuqq.setof.application.faqcategory.manager.command.FaqCategoryPersistenceManager;
import com.ryuqq.setof.application.faqcategory.manager.query.FaqCategoryReadManager;
import com.ryuqq.setof.application.faqcategory.port.in.command.DeleteFaqCategoryUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.faq.aggregate.FaqCategory;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryId;
import org.springframework.stereotype.Service;

/**
 * FAQ 카테고리 삭제 서비스
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class DeleteFaqCategoryService implements DeleteFaqCategoryUseCase {

    private final FaqCategoryPersistenceManager persistenceManager;
    private final FaqCategoryReadManager readManager;
    private final ClockHolder clockHolder;

    public DeleteFaqCategoryService(
            FaqCategoryPersistenceManager persistenceManager,
            FaqCategoryReadManager readManager,
            ClockHolder clockHolder) {
        this.persistenceManager = persistenceManager;
        this.readManager = readManager;
        this.clockHolder = clockHolder;
    }

    @Override
    public void execute(DeleteFaqCategoryCommand command) {
        FaqCategoryId categoryId = new FaqCategoryId(command.categoryId());
        FaqCategory category = readManager.findById(categoryId);

        category.softDelete(clockHolder.getClock().instant());
        persistenceManager.persist(category);
    }
}
