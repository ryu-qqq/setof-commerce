package com.ryuqq.setof.application.faq.port.out.query;

import com.ryuqq.setof.domain.faq.aggregate.Faq;
import com.ryuqq.setof.domain.faq.query.FaqSearchCriteria;
import java.util.List;

/**
 * Faq Query Port.
 *
 * <p>FAQ 조회 관련 Port-Out 인터페이스입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface FaqQueryPort {

    List<Faq> findByCriteria(FaqSearchCriteria criteria);
}
