package com.connectly.partnerAdmin.module.common.strategy.search;


import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.connectly.partnerAdmin.module.common.enums.SearchKeyword.POLICY_NAME;
import static com.connectly.partnerAdmin.module.discount.entity.QDiscountPolicy.discountPolicy;

@Component
public class DiscountPolicyNameSearchCondition extends AbstractSearchCondition{

    @Override
    public BooleanExpression apply(String searchWord) {
        return discountPolicy.discountDetails.discountPolicyName.like("%" + searchWord + "%");

    }

    @Override
    public SearchKeyword getSearchKeyword() {
        return POLICY_NAME;
    }
}
