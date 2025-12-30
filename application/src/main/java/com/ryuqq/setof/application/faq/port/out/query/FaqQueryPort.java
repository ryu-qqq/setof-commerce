package com.ryuqq.setof.application.faq.port.out.query;

import com.ryuqq.setof.domain.faq.aggregate.Faq;
import com.ryuqq.setof.domain.faq.query.FaqSearchCriteria;
import com.ryuqq.setof.domain.faq.vo.FaqId;
import java.util.List;
import java.util.Optional;

/**
 * FAQ 조회 Outbound Port (Query)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface FaqQueryPort {

    /**
     * FAQ ID로 조회
     *
     * @param faqId FAQ ID
     * @return FAQ Optional
     */
    Optional<Faq> findById(FaqId faqId);

    /**
     * FAQ 조건 조회
     *
     * @param criteria 검색 조건
     * @return FAQ 목록
     */
    List<Faq> findByCriteria(FaqSearchCriteria criteria);

    /**
     * FAQ 조건 개수 조회
     *
     * @param criteria 검색 조건
     * @return FAQ 개수
     */
    long countByCriteria(FaqSearchCriteria criteria);

    /**
     * FAQ 존재 여부 확인
     *
     * @param faqId FAQ ID
     * @return 존재 여부
     */
    boolean existsById(FaqId faqId);
}
