package com.ryuqq.setof.adapter.out.persistence.displaycomponent.adapter;

import com.ryuqq.setof.adapter.out.persistence.displaycomponent.entity.DisplayComponentJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.displaycomponent.entity.DisplayTabJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.displaycomponent.mapper.DisplayComponentJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.displaycomponent.repository.DisplayComponentQueryDslRepository;
import com.ryuqq.setof.application.contentpage.port.out.DisplayComponentQueryPort;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;
import com.ryuqq.setof.domain.contentpage.vo.ComponentType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "persistence.legacy.content.enabled", havingValue = "false")
public class DisplayComponentQueryAdapter implements DisplayComponentQueryPort {

    private final DisplayComponentQueryDslRepository queryDslRepository;
    private final DisplayComponentJpaEntityMapper mapper;

    public DisplayComponentQueryAdapter(
            DisplayComponentQueryDslRepository queryDslRepository,
            DisplayComponentJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public List<DisplayComponent> findByContentPage(ContentPageSearchCriteria criteria) {
        if (criteria.contentPageId() == null) {
            return List.of();
        }

        List<DisplayComponentJpaEntity> componentEntities =
                queryDslRepository.fetchComponentsByContentPageId(
                        criteria.contentPageId(), criteria.bypass());

        if (componentEntities.isEmpty()) {
            return List.of();
        }

        List<Long> tabComponentIds =
                componentEntities.stream()
                        .filter(e -> ComponentType.TAB.name().equals(e.getComponentType()))
                        .map(DisplayComponentJpaEntity::getId)
                        .toList();

        Map<Long, List<DisplayTabJpaEntity>> tabsByComponentId =
                fetchTabsGroupedByComponentId(tabComponentIds);

        List<DisplayComponent> result = new ArrayList<>(componentEntities.size());
        for (DisplayComponentJpaEntity entity : componentEntities) {
            List<DisplayTabJpaEntity> tabs =
                    tabsByComponentId.getOrDefault(entity.getId(), List.of());
            result.add(mapper.toDomain(entity, tabs));
        }
        return result;
    }

    private Map<Long, List<DisplayTabJpaEntity>> fetchTabsGroupedByComponentId(
            List<Long> tabComponentIds) {
        if (tabComponentIds.isEmpty()) {
            return Map.of();
        }
        List<DisplayTabJpaEntity> allTabs =
                queryDslRepository.fetchTabsByComponentIds(tabComponentIds);
        return allTabs.stream().collect(Collectors.groupingBy(DisplayTabJpaEntity::getComponentId));
    }
}
