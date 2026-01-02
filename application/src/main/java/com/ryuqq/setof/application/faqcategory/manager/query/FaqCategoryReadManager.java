package com.ryuqq.setof.application.faqcategory.manager.query;

import com.ryuqq.setof.application.faqcategory.port.out.query.FaqCategoryQueryPort;
import com.ryuqq.setof.domain.faq.aggregate.FaqCategory;
import com.ryuqq.setof.domain.faq.exception.FaqCategoryNotFoundException;
import com.ryuqq.setof.domain.faq.query.FaqCategorySearchCriteria;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryCode;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * FAQ 카테고리 Read Manager
 *
 * <p>FAQ 카테고리 조회 관련 비즈니스 로직을 처리합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class FaqCategoryReadManager {

    private final FaqCategoryQueryPort faqCategoryQueryPort;

    public FaqCategoryReadManager(FaqCategoryQueryPort faqCategoryQueryPort) {
        this.faqCategoryQueryPort = faqCategoryQueryPort;
    }

    /**
     * FAQ 카테고리 ID로 조회
     *
     * @param categoryId FAQ 카테고리 ID
     * @return FAQ 카테고리
     * @throws FaqCategoryNotFoundException FAQ 카테고리가 없는 경우
     */
    public FaqCategory findById(FaqCategoryId categoryId) {
        return faqCategoryQueryPort
                .findById(categoryId)
                .orElseThrow(() -> new FaqCategoryNotFoundException(categoryId.value()));
    }

    /**
     * FAQ 카테고리 코드로 조회
     *
     * @param code 카테고리 코드
     * @return FAQ 카테고리
     * @throws FaqCategoryNotFoundException FAQ 카테고리가 없는 경우
     */
    public FaqCategory findByCode(FaqCategoryCode code) {
        return faqCategoryQueryPort
                .findByCode(code)
                .orElseThrow(() -> new FaqCategoryNotFoundException(code));
    }

    /**
     * FAQ 카테고리 목록 조회
     *
     * @param criteria 검색 조건
     * @return FAQ 카테고리 목록
     */
    public List<FaqCategory> findByCriteria(FaqCategorySearchCriteria criteria) {
        return faqCategoryQueryPort.findByCriteria(criteria);
    }

    /**
     * FAQ 카테고리 개수 조회
     *
     * @param criteria 검색 조건
     * @return FAQ 카테고리 개수
     */
    public long countByCriteria(FaqCategorySearchCriteria criteria) {
        return faqCategoryQueryPort.countByCriteria(criteria);
    }

    /**
     * FAQ 카테고리 코드 존재 여부 확인
     *
     * @param code 카테고리 코드
     * @return 존재 여부
     */
    public boolean existsByCode(FaqCategoryCode code) {
        return faqCategoryQueryPort.existsByCode(code);
    }
}
