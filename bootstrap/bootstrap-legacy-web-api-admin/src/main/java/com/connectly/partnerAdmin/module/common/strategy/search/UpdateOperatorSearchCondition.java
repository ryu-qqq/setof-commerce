package com.connectly.partnerAdmin.module.common.strategy.search;


import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import static com.connectly.partnerAdmin.module.common.enums.SearchKeyword.UPDATE_OPERATOR;
import static com.connectly.partnerAdmin.module.product.entity.group.QProductGroup.productGroup;

@Component
public class UpdateOperatorSearchCondition extends AbstractSearchCondition{

    @Override
    public BooleanExpression apply(String searchWord) {
        return productGroup.updateOperator.eq(searchWord);
    }

    @Override
    public SearchKeyword getSearchKeyword() {
        return UPDATE_OPERATOR;
    }
}
