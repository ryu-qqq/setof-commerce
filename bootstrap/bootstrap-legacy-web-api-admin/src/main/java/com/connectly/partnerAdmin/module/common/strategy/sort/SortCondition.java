package com.connectly.partnerAdmin.module.common.strategy.sort;

import com.connectly.partnerAdmin.module.common.enums.SortField;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SortCondition {

    OrderSpecifier<?> apply(Order direction);
    OrderSpecifier<?> apply(Order direction, Path<?> path);

    SortField getSortField();
}
