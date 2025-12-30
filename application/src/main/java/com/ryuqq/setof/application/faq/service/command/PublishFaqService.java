package com.ryuqq.setof.application.faq.service.command;

import com.ryuqq.setof.application.faq.dto.command.PublishFaqCommand;
import com.ryuqq.setof.application.faq.manager.command.FaqPersistenceManager;
import com.ryuqq.setof.application.faq.manager.query.FaqReadManager;
import com.ryuqq.setof.application.faq.port.in.command.PublishFaqUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.faq.aggregate.Faq;
import com.ryuqq.setof.domain.faq.vo.FaqId;
import org.springframework.stereotype.Service;

/**
 * FAQ 게시 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class PublishFaqService implements PublishFaqUseCase {

    private final FaqReadManager faqReadManager;
    private final FaqPersistenceManager faqPersistenceManager;
    private final ClockHolder clockHolder;

    public PublishFaqService(
            FaqReadManager faqReadManager,
            FaqPersistenceManager faqPersistenceManager,
            ClockHolder clockHolder) {
        this.faqReadManager = faqReadManager;
        this.faqPersistenceManager = faqPersistenceManager;
        this.clockHolder = clockHolder;
    }

    @Override
    public void execute(PublishFaqCommand command) {
        FaqId faqId = FaqId.of(command.faqId());
        Faq faq = faqReadManager.findById(faqId);
        faq.publish(clockHolder.getClock().instant());
        faqPersistenceManager.persist(faq);
    }
}
