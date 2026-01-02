package com.ryuqq.setof.adapter.out.persistence.faqcategory.adapter;

import com.ryuqq.setof.adapter.out.persistence.faqcategory.entity.FaqCategoryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.faqcategory.mapper.FaqCategoryJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.faqcategory.repository.FaqCategoryJpaRepository;
import com.ryuqq.setof.application.faqcategory.port.out.command.FaqCategoryPersistencePort;
import com.ryuqq.setof.domain.faq.aggregate.FaqCategory;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryId;
import org.springframework.stereotype.Repository;

/**
 * FaqCategoryPersistenceAdapter - FAQ 카테고리 영속성 Adapter (Command)
 *
 * <p>FaqCategoryPersistencePort를 구현하여 FAQ 카테고리의 영속성 작업을 처리합니다.
 *
 * <p>저장/수정은 persist()로 통합 (JPA merge 활용)
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class FaqCategoryPersistenceAdapter implements FaqCategoryPersistencePort {

    private final FaqCategoryJpaRepository jpaRepository;
    private final FaqCategoryJpaEntityMapper mapper;

    public FaqCategoryPersistenceAdapter(
            FaqCategoryJpaRepository jpaRepository, FaqCategoryJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public FaqCategoryId persist(FaqCategory faqCategory) {
        FaqCategoryJpaEntity entity = mapper.toEntity(faqCategory);
        FaqCategoryJpaEntity saved = jpaRepository.save(entity);
        return FaqCategoryId.of(saved.getId());
    }
}
