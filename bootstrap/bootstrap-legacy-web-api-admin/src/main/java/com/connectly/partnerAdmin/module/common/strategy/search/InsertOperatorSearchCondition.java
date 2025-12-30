package com.connectly.partnerAdmin.module.common.strategy.search;


import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import static com.connectly.partnerAdmin.module.common.enums.SearchKeyword.INSERT_OPERATOR;
import static com.connectly.partnerAdmin.module.discount.entity.QDiscountTarget.discountTarget;

@Component
public class InsertOperatorSearchCondition extends AbstractSearchCondition{

    @Override
    public BooleanExpression apply(String searchWord) {
        return discountTarget.insertOperator.eq(searchWord);
    }

    @Override
    public SearchKeyword getSearchKeyword() {
        return INSERT_OPERATOR;
    }
}
