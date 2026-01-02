package com.connectly.partnerAdmin.module.common.strategy.sort;

import com.connectly.partnerAdmin.module.common.enums.SortField;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.stereotype.Component;

import static com.connectly.partnerAdmin.module.common.enums.SortField.DISCOUNT_POLICY_ID;
import static com.connectly.partnerAdmin.module.discount.entity.QDiscountPolicy.discountPolicy;

@Component
public class DiscountPolicyIdSortCondition extends AbstractSortCondition{

    @Override
    public OrderSpecifier<?> apply(Order direction) {
        return getSortedColumn(direction, discountPolicy, getSortField().getField());
    }

    @Override
    public SortField getSortField() {
        return DISCOUNT_POLICY_ID;
    }

}
