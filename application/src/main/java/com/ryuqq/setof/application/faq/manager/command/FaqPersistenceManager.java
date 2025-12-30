package com.ryuqq.setof.application.faq.manager.command;

import com.ryuqq.setof.application.faq.port.out.command.FaqPersistencePort;
import com.ryuqq.setof.domain.faq.aggregate.Faq;
import com.ryuqq.setof.domain.faq.vo.FaqId;
import org.springframework.stereotype.Component;

/**
 * FAQ Persistence Manager
 *
 * <p>FAQ 영속성 관련 비즈니스 로직을 처리합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class FaqPersistenceManager {

    private final FaqPersistencePort faqPersistencePort;

    public FaqPersistenceManager(FaqPersistencePort faqPersistencePort) {
        this.faqPersistencePort = faqPersistencePort;
    }

    /**
     * FAQ 저장/수정
     *
     * @param faq 저장할 FAQ
     * @return 저장된 FAQ ID
     */
    public FaqId persist(Faq faq) {
        return faqPersistencePort.persist(faq);
    }
}
