package com.ryuqq.setof.application.faq.service.command;

import com.ryuqq.setof.application.faq.dto.command.UnsetTopFaqCommand;
import com.ryuqq.setof.application.faq.manager.command.FaqPersistenceManager;
import com.ryuqq.setof.application.faq.manager.query.FaqReadManager;
import com.ryuqq.setof.application.faq.port.in.command.UnsetTopFaqUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.faq.aggregate.Faq;
import com.ryuqq.setof.domain.faq.vo.FaqId;
import org.springframework.stereotype.Service;

/**
 * FAQ 상단 노출 해제 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UnsetTopFaqService implements UnsetTopFaqUseCase {

    private final FaqReadManager faqReadManager;
    private final FaqPersistenceManager faqPersistenceManager;
    private final ClockHolder clockHolder;

    public UnsetTopFaqService(
            FaqReadManager faqReadManager,
            FaqPersistenceManager faqPersistenceManager,
            ClockHolder clockHolder) {
        this.faqReadManager = faqReadManager;
        this.faqPersistenceManager = faqPersistenceManager;
        this.clockHolder = clockHolder;
    }

    @Override
    public void execute(UnsetTopFaqCommand command) {
        FaqId faqId = FaqId.of(command.faqId());
        Faq faq = faqReadManager.findById(faqId);
        faq.unsetTop(clockHolder.getClock().instant());
        faqPersistenceManager.persist(faq);
    }
}
