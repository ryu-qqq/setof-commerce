package com.connectly.partnerAdmin.module.common.strategy.sort;

import com.connectly.partnerAdmin.module.common.enums.SortField;
import com.connectly.partnerAdmin.module.order.entity.QOrder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.stereotype.Component;

import static com.connectly.partnerAdmin.module.common.enums.SortField.CONTENT_ID;
import static com.connectly.partnerAdmin.module.common.enums.SortField.ORDER_ID;
import static com.connectly.partnerAdmin.module.order.entity.QOrder.order;


@Component
public class OrderIdSortCondition extends AbstractSortCondition{

    @Override
    public OrderSpecifier<?> apply(Order direction) {
        return getSortedColumn(direction, order, getSortField().getField());
    }

    @Override
    public SortField getSortField() {
        return ORDER_ID;
    }

}
