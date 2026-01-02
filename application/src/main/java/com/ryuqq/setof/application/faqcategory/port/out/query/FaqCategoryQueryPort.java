package com.ryuqq.setof.application.faqcategory.port.out.query;

import com.ryuqq.setof.domain.faq.aggregate.FaqCategory;
import com.ryuqq.setof.domain.faq.query.FaqCategorySearchCriteria;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryCode;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryId;
import java.util.List;
import java.util.Optional;

/**
 * FAQ 카테고리 조회 Outbound Port (Query)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface FaqCategoryQueryPort {

    /**
     * FAQ 카테고리 ID로 조회
     *
     * @param id FAQ 카테고리 ID
     * @return FAQ 카테고리 Optional
     */
    Optional<FaqCategory> findById(FaqCategoryId id);

    /**
     * FAQ 카테고리 코드로 조회
     *
     * @param code 카테고리 코드
     * @return FAQ 카테고리 Optional
     */
    Optional<FaqCategory> findByCode(FaqCategoryCode code);

    /**
     * FAQ 카테고리 조건 조회
     *
     * @param criteria 검색 조건
     * @return FAQ 카테고리 목록
     */
    List<FaqCategory> findByCriteria(FaqCategorySearchCriteria criteria);

    /**
     * FAQ 카테고리 조건 개수 조회
     *
     * @param criteria 검색 조건
     * @return FAQ 카테고리 개수
     */
    long countByCriteria(FaqCategorySearchCriteria criteria);

    /**
     * FAQ 카테고리 ID 존재 여부 확인
     *
     * @param id FAQ 카테고리 ID
     * @return 존재 여부
     */
    boolean existsById(FaqCategoryId id);

    /**
     * FAQ 카테고리 코드 존재 여부 확인
     *
     * @param code 카테고리 코드
     * @return 존재 여부
     */
    boolean existsByCode(FaqCategoryCode code);
}
