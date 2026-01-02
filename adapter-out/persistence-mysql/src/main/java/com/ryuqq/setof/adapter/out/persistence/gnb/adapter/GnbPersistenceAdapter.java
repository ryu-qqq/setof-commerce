package com.ryuqq.setof.adapter.out.persistence.gnb.adapter;

import com.ryuqq.setof.adapter.out.persistence.gnb.entity.GnbJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.gnb.mapper.GnbJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.gnb.repository.GnbJpaRepository;
import com.ryuqq.setof.application.gnb.port.out.command.GnbPersistencePort;
import com.ryuqq.setof.domain.cms.aggregate.gnb.Gnb;
import com.ryuqq.setof.domain.cms.vo.GnbId;
import org.springframework.stereotype.Repository;

/**
 * GnbPersistenceAdapter - GNB 영속성 Adapter (Command)
 *
 * <p>GnbPersistencePort를 구현하여 GNB의 영속성 작업을 처리합니다.
 *
 * <p>저장/수정은 persist()로 통합 (JPA merge 활용)
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class GnbPersistenceAdapter implements GnbPersistencePort {

    private final GnbJpaRepository jpaRepository;
    private final GnbJpaEntityMapper mapper;

    public GnbPersistenceAdapter(GnbJpaRepository jpaRepository, GnbJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public GnbId persist(Gnb gnb) {
        GnbJpaEntity entity = mapper.toEntity(gnb);
        GnbJpaEntity saved = jpaRepository.save(entity);
        return GnbId.of(saved.getId());
    }
}
