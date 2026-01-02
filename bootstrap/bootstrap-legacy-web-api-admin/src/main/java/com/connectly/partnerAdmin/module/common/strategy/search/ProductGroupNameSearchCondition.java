package com.connectly.partnerAdmin.module.common.strategy.search;

import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import static com.connectly.partnerAdmin.module.common.enums.SearchKeyword.PRODUCT_GROUP_NAME;
import static com.connectly.partnerAdmin.module.product.entity.group.QProductGroup.productGroup;

@Component
public class ProductGroupNameSearchCondition extends AbstractSearchCondition{
    @Override
    public BooleanExpression apply(String searchWord) {
        return productGroup.productGroupDetails.productGroupName.like("%" + searchWord + "%");
    }

    @Override
    public SearchKeyword getSearchKeyword() {
        return PRODUCT_GROUP_NAME;
    }
}
