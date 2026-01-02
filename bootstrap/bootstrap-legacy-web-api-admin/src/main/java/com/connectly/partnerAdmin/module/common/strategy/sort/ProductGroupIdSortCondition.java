package com.connectly.partnerAdmin.module.common.strategy.sort;

import com.connectly.partnerAdmin.module.common.enums.SortField;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.stereotype.Component;

import static com.connectly.partnerAdmin.module.common.enums.SortField.PRODUCT_GROUP_ID;
import static com.connectly.partnerAdmin.module.product.entity.group.QProductGroup.productGroup;

@Component
public class ProductGroupIdSortCondition extends AbstractSortCondition{

    @Override
    public OrderSpecifier<?> apply(Order direction) {
        return getSortedColumn(direction, productGroup, getSortField().getField());
    }

    @Override
    public SortField getSortField() {
        return PRODUCT_GROUP_ID;
    }

}
