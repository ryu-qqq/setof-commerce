package com.ryuqq.setof.adapter.out.persistence.displaycomponent.condition;

import static com.ryuqq.setof.adapter.out.persistence.displaycomponent.entity.QDisplayComponentJpaEntity.displayComponentJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.displaycomponent.entity.QDisplayTabJpaEntity.displayTabJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DisplayComponentConditionBuilder {

    // ── DisplayComponent 조건 ──

    public BooleanExpression componentContentPageIdEq(long contentPageId) {
        return displayComponentJpaEntity.contentPageId.eq(contentPageId);
    }

    public BooleanExpression componentActiveEq(boolean active) {
        return displayComponentJpaEntity.active.eq(active);
    }

    public BooleanExpression componentNotDeleted() {
        return displayComponentJpaEntity.deletedAt.isNull();
    }

    // ── DisplayTab 조건 ──

    public BooleanExpression displayTabComponentIdIn(List<Long> componentIds) {
        return componentIds != null && !componentIds.isEmpty()
                ? displayTabJpaEntity.componentId.in(componentIds)
                : null;
    }

    public BooleanExpression displayTabNotDeleted() {
        return displayTabJpaEntity.deletedAt.isNull();
    }
}
