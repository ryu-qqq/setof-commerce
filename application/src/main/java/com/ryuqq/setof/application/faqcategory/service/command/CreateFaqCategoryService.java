package com.ryuqq.setof.application.faqcategory.service.command;

import com.ryuqq.setof.application.faqcategory.dto.command.CreateFaqCategoryCommand;
import com.ryuqq.setof.application.faqcategory.factory.command.FaqCategoryCommandFactory;
import com.ryuqq.setof.application.faqcategory.manager.command.FaqCategoryPersistenceManager;
import com.ryuqq.setof.application.faqcategory.manager.query.FaqCategoryReadManager;
import com.ryuqq.setof.application.faqcategory.port.in.command.CreateFaqCategoryUseCase;
import com.ryuqq.setof.domain.faq.aggregate.FaqCategory;
import com.ryuqq.setof.domain.faq.exception.DuplicateFaqCategoryCodeException;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryCode;
import org.springframework.stereotype.Service;

/**
 * FAQ 카테고리 생성 서비스
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class CreateFaqCategoryService implements CreateFaqCategoryUseCase {

    private final FaqCategoryCommandFactory commandFactory;
    private final FaqCategoryPersistenceManager persistenceManager;
    private final FaqCategoryReadManager readManager;

    public CreateFaqCategoryService(
            FaqCategoryCommandFactory commandFactory,
            FaqCategoryPersistenceManager persistenceManager,
            FaqCategoryReadManager readManager) {
        this.commandFactory = commandFactory;
        this.persistenceManager = persistenceManager;
        this.readManager = readManager;
    }

    @Override
    public Long execute(CreateFaqCategoryCommand command) {
        validateCodeUniqueness(command.code());

        FaqCategory category = commandFactory.createFaqCategory(command);
        return persistenceManager.persist(category).value();
    }

    private void validateCodeUniqueness(String code) {
        FaqCategoryCode categoryCode = new FaqCategoryCode(code);
        if (readManager.existsByCode(categoryCode)) {
            throw new DuplicateFaqCategoryCodeException(code);
        }
    }
}
