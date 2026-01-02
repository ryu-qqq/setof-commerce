package com.ryuqq.setof.application.faq.service.command;

import com.ryuqq.setof.application.faq.dto.command.SetTopFaqCommand;
import com.ryuqq.setof.application.faq.manager.command.FaqPersistenceManager;
import com.ryuqq.setof.application.faq.manager.query.FaqReadManager;
import com.ryuqq.setof.application.faq.port.in.command.SetTopFaqUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.faq.aggregate.Faq;
import com.ryuqq.setof.domain.faq.vo.FaqId;
import org.springframework.stereotype.Service;

/**
 * FAQ 상단 노출 설정 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class SetTopFaqService implements SetTopFaqUseCase {

    private final FaqReadManager faqReadManager;
    private final FaqPersistenceManager faqPersistenceManager;
    private final ClockHolder clockHolder;

    public SetTopFaqService(
            FaqReadManager faqReadManager,
            FaqPersistenceManager faqPersistenceManager,
            ClockHolder clockHolder) {
        this.faqReadManager = faqReadManager;
        this.faqPersistenceManager = faqPersistenceManager;
        this.clockHolder = clockHolder;
    }

    @Override
    public void execute(SetTopFaqCommand command) {
        FaqId faqId = FaqId.of(command.faqId());
        Faq faq = faqReadManager.findById(faqId);
        faq.setTop(command.topOrder(), clockHolder.getClock().instant());
        faqPersistenceManager.persist(faq);
    }
}
