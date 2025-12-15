package com.setof.connectly.module.display.repository.component.item.fetch;

import static com.setof.connectly.module.display.entity.component.QComponentTarget.componentTarget;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.display.entity.component.ComponentTarget;
import com.setof.connectly.module.display.enums.SortType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ComponentTargetFindRepositoryImpl implements ComponentTargetFindRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ComponentTarget> fetchComponentTarget(
            long componentId, SortType sortType, Long tabId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(componentTarget)
                        .where(
                                componentTargetHasComponentId(componentId),
                                sortTypeEq(sortType),
                                tabIdEq(tabId))
                        .fetchOne());
    }

    private BooleanExpression componentTargetHasComponentId(long componentId) {
        return componentTarget.componentId.eq(componentId);
    }

    private BooleanExpression tabIdEq(Long tabId) {
        if (tabId != null) return componentTarget.tabId.eq(tabId);
        else return null;
    }

    private BooleanExpression sortTypeEq(SortType sortType) {
        return componentTarget.sortType.eq(sortType);
    }
}
