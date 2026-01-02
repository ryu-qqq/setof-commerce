package com.ryuqq.setof.application.faq.service.command;

import com.ryuqq.setof.application.faq.dto.command.ChangeFaqCategoryCommand;
import com.ryuqq.setof.application.faq.manager.command.FaqPersistenceManager;
import com.ryuqq.setof.application.faq.manager.query.FaqReadManager;
import com.ryuqq.setof.application.faq.port.in.command.ChangeFaqCategoryUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.faq.aggregate.Faq;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryCode;
import com.ryuqq.setof.domain.faq.vo.FaqId;
import org.springframework.stereotype.Service;

/**
 * FAQ 카테고리 변경 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class ChangeFaqCategoryService implements ChangeFaqCategoryUseCase {

    private final FaqReadManager faqReadManager;
    private final FaqPersistenceManager faqPersistenceManager;
    private final ClockHolder clockHolder;

    public ChangeFaqCategoryService(
            FaqReadManager faqReadManager,
            FaqPersistenceManager faqPersistenceManager,
            ClockHolder clockHolder) {
        this.faqReadManager = faqReadManager;
        this.faqPersistenceManager = faqPersistenceManager;
        this.clockHolder = clockHolder;
    }

    @Override
    public void execute(ChangeFaqCategoryCommand command) {
        FaqId faqId = FaqId.of(command.faqId());
        Faq faq = faqReadManager.findById(faqId);
        FaqCategoryCode newCategoryCode = FaqCategoryCode.of(command.newCategoryCode());
        Faq updatedFaq = faq.changeCategory(newCategoryCode, clockHolder.getClock().instant());
        faqPersistenceManager.persist(updatedFaq);
    }
}
