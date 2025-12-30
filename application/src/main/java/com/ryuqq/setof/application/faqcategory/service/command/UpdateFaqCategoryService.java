package com.ryuqq.setof.application.faqcategory.service.command;

import com.ryuqq.setof.application.faqcategory.dto.command.UpdateFaqCategoryCommand;
import com.ryuqq.setof.application.faqcategory.factory.command.FaqCategoryCommandFactory;
import com.ryuqq.setof.application.faqcategory.manager.command.FaqCategoryPersistenceManager;
import com.ryuqq.setof.application.faqcategory.manager.query.FaqCategoryReadManager;
import com.ryuqq.setof.application.faqcategory.port.in.command.UpdateFaqCategoryUseCase;
import com.ryuqq.setof.domain.faq.aggregate.FaqCategory;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryId;
import org.springframework.stereotype.Service;

/**
 * FAQ 카테고리 수정 서비스
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateFaqCategoryService implements UpdateFaqCategoryUseCase {

    private final FaqCategoryCommandFactory commandFactory;
    private final FaqCategoryPersistenceManager persistenceManager;
    private final FaqCategoryReadManager readManager;

    public UpdateFaqCategoryService(
            FaqCategoryCommandFactory commandFactory,
            FaqCategoryPersistenceManager persistenceManager,
            FaqCategoryReadManager readManager) {
        this.commandFactory = commandFactory;
        this.persistenceManager = persistenceManager;
        this.readManager = readManager;
    }

    @Override
    public void execute(UpdateFaqCategoryCommand command) {
        FaqCategoryId categoryId = new FaqCategoryId(command.categoryId());
        FaqCategory category = readManager.findById(categoryId);

        FaqCategory updatedCategory = commandFactory.applyUpdate(category, command);
        persistenceManager.persist(updatedCategory);
    }
}
