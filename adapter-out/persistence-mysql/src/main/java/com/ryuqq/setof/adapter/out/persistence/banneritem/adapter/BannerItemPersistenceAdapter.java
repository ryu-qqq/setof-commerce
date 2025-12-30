package com.ryuqq.setof.adapter.out.persistence.banneritem.adapter;

import com.ryuqq.setof.adapter.out.persistence.banneritem.entity.CmsBannerItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.banneritem.mapper.BannerItemJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.banneritem.repository.CmsBannerItemJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.banneritem.repository.CmsBannerItemQueryDslRepository;
import com.ryuqq.setof.application.banneritem.port.out.command.BannerItemPersistencePort;
import com.ryuqq.setof.domain.cms.aggregate.banner.BannerItem;
import com.ryuqq.setof.domain.cms.vo.BannerId;
import com.ryuqq.setof.domain.cms.vo.BannerItemId;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * BannerItemPersistenceAdapter - BannerItem 영속성 Adapter
 *
 * <p>BannerItemPersistencePort 구현체로서 BannerItem을 데이터베이스에 저장합니다.
 *
 * <p><strong>Repository 분리:</strong>
 *
 * <ul>
 *   <li>JpaRepository: 표준 CRUD (save, saveAll, delete)
 *   <li>QueryDslRepository: 커스텀 쿼리 (findBy*, deleteBy*)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class BannerItemPersistenceAdapter implements BannerItemPersistencePort {

    private final CmsBannerItemJpaRepository jpaRepository;
    private final CmsBannerItemQueryDslRepository queryDslRepository;
    private final BannerItemJpaEntityMapper mapper;

    public BannerItemPersistenceAdapter(
            CmsBannerItemJpaRepository jpaRepository,
            CmsBannerItemQueryDslRepository queryDslRepository,
            BannerItemJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public BannerItemId persist(BannerItem bannerItem) {
        CmsBannerItemJpaEntity entity = mapper.toEntity(bannerItem);
        CmsBannerItemJpaEntity savedEntity = jpaRepository.save(entity);
        return BannerItemId.of(savedEntity.getId());
    }

    @Override
    public List<BannerItemId> persistAll(List<BannerItem> bannerItems) {
        List<CmsBannerItemJpaEntity> entities = mapper.toEntityList(bannerItems);
        List<CmsBannerItemJpaEntity> savedEntities = jpaRepository.saveAll(entities);
        return savedEntities.stream().map(entity -> BannerItemId.of(entity.getId())).toList();
    }

    @Override
    public void deleteAllByBannerId(BannerId bannerId) {
        List<CmsBannerItemJpaEntity> entities =
                queryDslRepository.findAllByBannerId(bannerId.value());
        for (CmsBannerItemJpaEntity entity : entities) {
            // Soft Delete: 도메인에서 상태 변경 후 저장
            BannerItem item = mapper.toDomain(entity);
            item.delete();
            jpaRepository.save(mapper.toEntity(item));
        }
    }
}
