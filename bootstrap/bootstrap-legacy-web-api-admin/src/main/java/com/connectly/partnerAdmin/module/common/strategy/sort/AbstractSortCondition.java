package com.connectly.partnerAdmin.module.common.strategy.sort;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractSortCondition implements SortCondition {

    @Override
    public OrderSpecifier<?> apply(Order direction) {
        return null;
    }

    @Override
    public OrderSpecifier<?> apply(Order direction, Path<?> path) {
        return getSortedColumn(direction, path, getSortField().getField());
    }


    protected OrderSpecifier<?> getSortedColumn(Order direction, Path<?> entityPath, String fieldName){
        Path<Object> fieldPath = Expressions.path(Object.class, entityPath, fieldName);
        return new OrderSpecifier(direction, fieldPath);
    }

}
