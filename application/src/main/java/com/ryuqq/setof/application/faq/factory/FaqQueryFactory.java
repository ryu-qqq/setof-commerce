package com.ryuqq.setof.application.faq.factory;

import com.ryuqq.setof.application.faq.dto.query.FaqSearchParams;
import com.ryuqq.setof.domain.faq.query.FaqSearchCriteria;
import org.springframework.stereotype.Component;

/**
 * Faq Query Factory.
 *
 * <p>Query DTO를 Domain Criteria로 변환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class FaqQueryFactory {

    /**
     * FaqSearchParams로부터 FaqSearchCriteria 생성.
     *
     * @param params 검색 파라미터
     * @return FaqSearchCriteria
     */
    public FaqSearchCriteria createCriteria(FaqSearchParams params) {
        return FaqSearchCriteria.of(params.faqType());
    }
}
