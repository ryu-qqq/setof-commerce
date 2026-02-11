package com.ryuqq.setof.application.faq.manager;

import com.ryuqq.setof.application.faq.port.out.query.FaqQueryPort;
import com.ryuqq.setof.domain.faq.aggregate.Faq;
import com.ryuqq.setof.domain.faq.query.FaqSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Faq Read Manager.
 *
 * <p>FAQ 조회 관련 Manager입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class FaqReadManager {

    private final FaqQueryPort queryPort;

    public FaqReadManager(FaqQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    @Transactional(readOnly = true)
    public List<Faq> findByCriteria(FaqSearchCriteria criteria) {
        return queryPort.findByCriteria(criteria);
    }
}
