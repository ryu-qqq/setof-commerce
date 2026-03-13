package com.ryuqq.setof.adapter.out.persistence.displaycomponent.repository;

import static com.ryuqq.setof.adapter.out.persistence.displaycomponent.entity.QDisplayComponentJpaEntity.displayComponentJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.displaycomponent.entity.QDisplayTabJpaEntity.displayTabJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.displaycomponent.condition.DisplayComponentConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.displaycomponent.entity.DisplayComponentJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.displaycomponent.entity.DisplayTabJpaEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class DisplayComponentQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final DisplayComponentConditionBuilder conditionBuilder;

    public DisplayComponentQueryDslRepository(
            JPAQueryFactory queryFactory, DisplayComponentConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    public List<DisplayComponentJpaEntity> fetchComponentsByContentPageId(
            long contentPageId, boolean bypass) {
        return queryFactory
                .selectFrom(displayComponentJpaEntity)
                .where(
                        conditionBuilder.componentContentPageIdEq(contentPageId),
                        bypass ? null : conditionBuilder.componentActiveEq(true),
                        conditionBuilder.componentNotDeleted())
                .orderBy(displayComponentJpaEntity.displayOrder.asc())
                .fetch();
    }

    public List<DisplayTabJpaEntity> fetchTabsByComponentIds(List<Long> componentIds) {
        if (componentIds == null || componentIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .selectFrom(displayTabJpaEntity)
                .where(
                        conditionBuilder.displayTabComponentIdIn(componentIds),
                        conditionBuilder.displayTabNotDeleted())
                .orderBy(displayTabJpaEntity.displayOrder.asc())
                .fetch();
    }
}
