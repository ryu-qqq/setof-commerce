package com.ryuqq.setof.adapter.out.persistence.faq.adapter;

import com.ryuqq.setof.adapter.out.persistence.faq.entity.FaqJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.faq.mapper.FaqJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.faq.repository.FaqJpaRepository;
import com.ryuqq.setof.application.faq.port.out.command.FaqPersistencePort;
import com.ryuqq.setof.domain.faq.aggregate.Faq;
import com.ryuqq.setof.domain.faq.vo.FaqId;
import org.springframework.stereotype.Repository;

/**
 * FaqPersistenceAdapter - FAQ 영속성 Adapter (Command)
 *
 * <p>FaqPersistencePort를 구현하여 FAQ의 영속성 작업을 처리합니다.
 *
 * <p>저장/수정은 persist()로 통합 (JPA merge 활용)
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class FaqPersistenceAdapter implements FaqPersistencePort {

    private final FaqJpaRepository jpaRepository;
    private final FaqJpaEntityMapper mapper;

    public FaqPersistenceAdapter(FaqJpaRepository jpaRepository, FaqJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public FaqId persist(Faq faq) {
        FaqJpaEntity entity = mapper.toEntity(faq);
        FaqJpaEntity saved = jpaRepository.save(entity);
        return FaqId.of(saved.getId());
    }
}
