package com.connectly.partnerAdmin.module.common.strategy.sort;

import com.connectly.partnerAdmin.module.common.enums.SortField;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import org.springframework.stereotype.Component;

import static com.connectly.partnerAdmin.module.common.enums.SortField.INSERT_DATE;

@Component
public class InsertDateSortCondition extends AbstractSortCondition{


    @Override
    public OrderSpecifier<?> apply(Order direction, Path<?> path) {
        return super.apply(direction, path);
    }

    @Override
    public SortField getSortField() {
        return INSERT_DATE;
    }
}
