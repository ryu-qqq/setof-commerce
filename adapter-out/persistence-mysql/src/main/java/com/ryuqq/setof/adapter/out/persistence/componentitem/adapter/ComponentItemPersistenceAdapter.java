package com.ryuqq.setof.adapter.out.persistence.componentitem.adapter;

import com.ryuqq.setof.adapter.out.persistence.componentitem.entity.CmsComponentItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.componentitem.mapper.ComponentItemJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.componentitem.repository.CmsComponentItemJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.componentitem.repository.CmsComponentItemQueryDslRepository;
import com.ryuqq.setof.application.componentitem.port.out.command.ComponentItemPersistencePort;
import com.ryuqq.setof.domain.cms.aggregate.component.ComponentItem;
import com.ryuqq.setof.domain.cms.vo.ComponentId;
import com.ryuqq.setof.domain.cms.vo.ComponentItemId;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * ComponentItemPersistenceAdapter - ComponentItem 영속성 Adapter
 *
 * <p>ComponentItemPersistencePort 구현체로서 ComponentItem을 데이터베이스에 저장합니다.
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
public class ComponentItemPersistenceAdapter implements ComponentItemPersistencePort {

    private final CmsComponentItemJpaRepository jpaRepository;
    private final CmsComponentItemQueryDslRepository queryDslRepository;
    private final ComponentItemJpaEntityMapper mapper;

    public ComponentItemPersistenceAdapter(
            CmsComponentItemJpaRepository jpaRepository,
            CmsComponentItemQueryDslRepository queryDslRepository,
            ComponentItemJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public ComponentItemId persist(ComponentItem componentItem) {
        CmsComponentItemJpaEntity entity = mapper.toEntity(componentItem);
        CmsComponentItemJpaEntity savedEntity = jpaRepository.save(entity);
        return ComponentItemId.of(savedEntity.getId());
    }

    @Override
    public List<ComponentItemId> persistAll(List<ComponentItem> componentItems) {
        List<CmsComponentItemJpaEntity> entities = mapper.toEntityList(componentItems);
        List<CmsComponentItemJpaEntity> savedEntities = jpaRepository.saveAll(entities);
        return savedEntities.stream().map(entity -> ComponentItemId.of(entity.getId())).toList();
    }

    @Override
    public void deleteAllByComponentId(ComponentId componentId) {
        List<CmsComponentItemJpaEntity> entities =
                queryDslRepository.findAllByComponentId(componentId.value());
        for (CmsComponentItemJpaEntity entity : entities) {
            // Soft Delete: 도메인에서 상태 변경 후 저장하는 패턴 사용
            ComponentItem item = mapper.toDomain(entity);
            item.delete();
            jpaRepository.save(mapper.toEntity(item));
        }
    }
}
