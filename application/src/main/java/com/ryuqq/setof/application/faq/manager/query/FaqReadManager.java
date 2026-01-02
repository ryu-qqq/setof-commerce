package com.ryuqq.setof.application.faq.manager.query;

import com.ryuqq.setof.application.faq.port.out.query.FaqQueryPort;
import com.ryuqq.setof.domain.faq.aggregate.Faq;
import com.ryuqq.setof.domain.faq.exception.FaqNotFoundException;
import com.ryuqq.setof.domain.faq.query.FaqSearchCriteria;
import com.ryuqq.setof.domain.faq.vo.FaqId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * FAQ Read Manager
 *
 * <p>FAQ 조회 관련 비즈니스 로직을 처리합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class FaqReadManager {

    private final FaqQueryPort faqQueryPort;

    public FaqReadManager(FaqQueryPort faqQueryPort) {
        this.faqQueryPort = faqQueryPort;
    }

    /**
     * FAQ ID로 조회
     *
     * @param faqId FAQ ID
     * @return FAQ
     * @throws FaqNotFoundException FAQ가 없는 경우
     */
    public Faq findById(FaqId faqId) {
        return faqQueryPort.findById(faqId).orElseThrow(() -> new FaqNotFoundException(faqId));
    }

    /**
     * FAQ 목록 조회
     *
     * @param criteria 검색 조건
     * @return FAQ 목록
     */
    public List<Faq> findByCriteria(FaqSearchCriteria criteria) {
        return faqQueryPort.findByCriteria(criteria);
    }

    /**
     * FAQ 개수 조회
     *
     * @param criteria 검색 조건
     * @return FAQ 개수
     */
    public long countByCriteria(FaqSearchCriteria criteria) {
        return faqQueryPort.countByCriteria(criteria);
    }

    /**
     * FAQ 존재 여부 확인
     *
     * @param faqId FAQ ID
     * @return 존재 여부
     */
    public boolean existsById(FaqId faqId) {
        return faqQueryPort.existsById(faqId);
    }
}
