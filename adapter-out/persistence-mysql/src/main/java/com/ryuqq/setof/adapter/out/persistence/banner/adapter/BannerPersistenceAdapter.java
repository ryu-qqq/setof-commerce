package com.ryuqq.setof.adapter.out.persistence.banner.adapter;

import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.banner.mapper.BannerJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.banner.repository.BannerJpaRepository;
import com.ryuqq.setof.application.banner.port.out.command.BannerPersistencePort;
import com.ryuqq.setof.domain.cms.aggregate.banner.Banner;
import com.ryuqq.setof.domain.cms.vo.BannerId;
import org.springframework.stereotype.Repository;

/**
 * BannerPersistenceAdapter - Banner 영속성 Adapter (Command)
 *
 * <p>BannerPersistencePort를 구현하여 Banner의 영속성 작업을 처리합니다.
 *
 * <p>저장/수정은 persist()로 통합 (JPA merge 활용)
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class BannerPersistenceAdapter implements BannerPersistencePort {

    private final BannerJpaRepository jpaRepository;
    private final BannerJpaEntityMapper mapper;

    public BannerPersistenceAdapter(
            BannerJpaRepository jpaRepository, BannerJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public BannerId persist(Banner banner) {
        BannerJpaEntity entity = mapper.toEntity(banner);
        BannerJpaEntity saved = jpaRepository.save(entity);
        return BannerId.of(saved.getId());
    }
}
