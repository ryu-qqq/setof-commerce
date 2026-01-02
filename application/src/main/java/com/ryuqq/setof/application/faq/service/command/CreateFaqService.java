package com.ryuqq.setof.application.faq.service.command;

import com.ryuqq.setof.application.faq.dto.command.CreateFaqCommand;
import com.ryuqq.setof.application.faq.factory.command.FaqCommandFactory;
import com.ryuqq.setof.application.faq.manager.command.FaqPersistenceManager;
import com.ryuqq.setof.application.faq.port.in.command.CreateFaqUseCase;
import com.ryuqq.setof.domain.faq.aggregate.Faq;
import org.springframework.stereotype.Service;

/**
 * FAQ 생성 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class CreateFaqService implements CreateFaqUseCase {

    private final FaqCommandFactory faqCommandFactory;
    private final FaqPersistenceManager faqPersistenceManager;

    public CreateFaqService(
            FaqCommandFactory faqCommandFactory, FaqPersistenceManager faqPersistenceManager) {
        this.faqCommandFactory = faqCommandFactory;
        this.faqPersistenceManager = faqPersistenceManager;
    }

    @Override
    public Long execute(CreateFaqCommand command) {
        Faq faq = faqCommandFactory.createFaq(command);
        return faqPersistenceManager.persist(faq).value();
    }
}
