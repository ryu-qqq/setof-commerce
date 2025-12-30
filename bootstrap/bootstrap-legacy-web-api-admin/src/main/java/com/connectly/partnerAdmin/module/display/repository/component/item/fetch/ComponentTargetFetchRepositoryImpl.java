package com.connectly.partnerAdmin.module.display.repository.component.item.fetch;


import com.connectly.partnerAdmin.module.display.entity.component.ComponentTarget;
import com.connectly.partnerAdmin.module.display.enums.SortType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.connectly.partnerAdmin.module.display.entity.component.QComponentTarget.componentTarget;


@Repository
@RequiredArgsConstructor
public class ComponentTargetFetchRepositoryImpl implements ComponentTargetFetchRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ComponentTarget> fetchComponentTarget(long componentId, SortType sortType, Long tabId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(componentTarget)
                        .where(componentTargetHasComponentId(componentId), sortTypeEq(sortType), tabIdEq(tabId))
                        .fetchOne()
        );
    }


    private BooleanExpression componentTargetHasComponentId(long componentId){
        return componentTarget.componentId.eq(componentId);
    }

    private BooleanExpression tabIdEq(Long tabId){
        if(tabId != null) return componentTarget.tabId.eq(tabId);
        else return null;
    }

    private BooleanExpression sortTypeEq(SortType sortType){
        return componentTarget.sortType.eq(sortType);
    }
}
