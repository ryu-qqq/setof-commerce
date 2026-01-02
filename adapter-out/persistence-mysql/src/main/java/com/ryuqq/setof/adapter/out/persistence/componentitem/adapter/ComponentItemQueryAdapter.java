package com.ryuqq.setof.adapter.out.persistence.componentitem.adapter;

import com.ryuqq.setof.adapter.out.persistence.componentitem.entity.CmsComponentItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.componentitem.mapper.ComponentItemJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.componentitem.repository.CmsComponentItemQueryDslRepository;
import com.ryuqq.setof.application.componentitem.port.out.query.ComponentItemQueryPort;
import com.ryuqq.setof.domain.cms.aggregate.component.ComponentItem;
import com.ryuqq.setof.domain.cms.vo.ComponentId;
import com.ryuqq.setof.domain.cms.vo.ComponentItemId;
import com.ryuqq.setof.domain.cms.vo.ComponentItemType;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * ComponentItemQueryAdapter - ComponentItem 조회 Adapter
 *
 * <p>ComponentItemQueryPort 구현체로서 데이터베이스에서 ComponentItem을 조회합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class ComponentItemQueryAdapter implements ComponentItemQueryPort {

    private final CmsComponentItemQueryDslRepository queryDslRepository;
    private final ComponentItemJpaEntityMapper mapper;

    public ComponentItemQueryAdapter(
            CmsComponentItemQueryDslRepository queryDslRepository,
            ComponentItemJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<ComponentItem> findById(ComponentItemId componentItemId) {
        return queryDslRepository.findById(componentItemId.value()).map(mapper::toDomain);
    }

    @Override
    public List<ComponentItem> findActiveByComponentId(ComponentId componentId) {
        List<CmsComponentItemJpaEntity> entities =
                queryDslRepository.findActiveByComponentId(componentId.value());
        return mapper.toDomainList(entities);
    }

    @Override
    public List<ComponentItem> findAllByComponentId(ComponentId componentId) {
        List<CmsComponentItemJpaEntity> entities =
                queryDslRepository.findAllByComponentId(componentId.value());
        return mapper.toDomainList(entities);
    }

    @Override
    public List<ComponentItem> findActiveByComponentIds(List<ComponentId> componentIds) {
        List<Long> ids = componentIds.stream().map(ComponentId::value).toList();
        List<CmsComponentItemJpaEntity> entities = queryDslRepository.findActiveByComponentIds(ids);
        return mapper.toDomainList(entities);
    }

    @Override
    public List<ComponentItem> findByReferenceIdAndType(
            Long referenceId, ComponentItemType itemType) {
        List<CmsComponentItemJpaEntity> entities =
                queryDslRepository.findByReferenceIdAndType(referenceId, itemType.name());
        return mapper.toDomainList(entities);
    }
}
