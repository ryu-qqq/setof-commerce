package com.ryuqq.setof.adapter.out.persistence.component.adapter;

import com.ryuqq.setof.adapter.out.persistence.component.entity.ComponentJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.component.mapper.ComponentJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.component.repository.ComponentJpaRepository;
import com.ryuqq.setof.application.component.port.out.command.ComponentPersistencePort;
import com.ryuqq.setof.domain.cms.aggregate.component.Component;
import com.ryuqq.setof.domain.cms.vo.ComponentId;
import org.springframework.stereotype.Repository;

/**
 * ComponentPersistenceAdapter - Component 영속성 Adapter (Command)
 *
 * <p>ComponentPersistencePort를 구현하여 Component의 영속성 작업을 처리합니다.
 *
 * <p>저장/수정은 persist()로 통합 (JPA merge 활용)
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class ComponentPersistenceAdapter implements ComponentPersistencePort {

    private final ComponentJpaRepository jpaRepository;
    private final ComponentJpaEntityMapper mapper;

    public ComponentPersistenceAdapter(
            ComponentJpaRepository jpaRepository, ComponentJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public ComponentId persist(Component component) {
        ComponentJpaEntity entity = mapper.toEntity(component);
        ComponentJpaEntity saved = jpaRepository.save(entity);
        return ComponentId.of(saved.getId());
    }
}
