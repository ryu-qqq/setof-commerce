package com.connectly.partnerAdmin.module.common.strategy.sort;

import com.connectly.partnerAdmin.module.common.enums.SortField;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.connectly.partnerAdmin.module.common.enums.SortField.UPDATE_DATE;

@Component
public class UpdateDateSortCondition extends AbstractSortCondition{

    @Override
    public OrderSpecifier<?> apply(Order direction, Path<?> path) {
        return super.apply(direction, path);
    }

    @Override
    public SortField getSortField() {
        return UPDATE_DATE;
    }
}
