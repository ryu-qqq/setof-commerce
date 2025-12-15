package com.setof.connectly.module.common.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.setof.connectly.module.display.enums.component.OrderType;
import java.util.List;

public interface CommonRepository {

    List<OrderSpecifier<?>> createOrderSpecifiersFromPageable(
            EntityPathBase<?> entityPath, OrderType orderType);
}
