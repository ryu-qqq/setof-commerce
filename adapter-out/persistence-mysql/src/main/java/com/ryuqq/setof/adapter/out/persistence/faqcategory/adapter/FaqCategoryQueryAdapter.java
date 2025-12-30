package com.ryuqq.setof.adapter.out.persistence.faqcategory.adapter;

import com.ryuqq.setof.adapter.out.persistence.faqcategory.mapper.FaqCategoryJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.faqcategory.repository.FaqCategoryQueryDslRepository;
import com.ryuqq.setof.application.faqcategory.port.out.query.FaqCategoryQueryPort;
import com.ryuqq.setof.domain.faq.aggregate.FaqCategory;
import com.ryuqq.setof.domain.faq.query.FaqCategorySearchCriteria;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryCode;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * FaqCategoryQueryAdapter - FAQ 카테고리 조회 Adapter (Query)
 *
 * <p>FaqCategoryQueryPort를 구현하여 FAQ 카테고리의 조회 작업을 처리합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class FaqCategoryQueryAdapter implements FaqCategoryQueryPort {

    private final FaqCategoryQueryDslRepository queryDslRepository;
    private final FaqCategoryJpaEntityMapper mapper;

    public FaqCategoryQueryAdapter(
            FaqCategoryQueryDslRepository queryDslRepository, FaqCategoryJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<FaqCategory> findById(FaqCategoryId id) {
        return queryDslRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public Optional<FaqCategory> findByCode(FaqCategoryCode code) {
        return queryDslRepository.findByCode(code.value()).map(mapper::toDomain);
    }

    @Override
    public List<FaqCategory> findByCriteria(FaqCategorySearchCriteria criteria) {
        return queryDslRepository.findByCriteria(criteria).stream().map(mapper::toDomain).toList();
    }

    @Override
    public long countByCriteria(FaqCategorySearchCriteria criteria) {
        return queryDslRepository.countByCriteria(criteria);
    }

    @Override
    public boolean existsById(FaqCategoryId id) {
        return queryDslRepository.existsById(id.value());
    }

    @Override
    public boolean existsByCode(FaqCategoryCode code) {
        return queryDslRepository.existsByCode(code.value());
    }
}
