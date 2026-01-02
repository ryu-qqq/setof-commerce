package com.connectly.partnerAdmin.module.common.strategy.search;

import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import static com.connectly.partnerAdmin.module.common.enums.SearchKeyword.EXCEL_PRODUCT_GROUP_ID;
import static com.connectly.partnerAdmin.module.product.entity.group.QProductGroup.productGroup;

@Component
public class ExcelProductSearchCondition extends AbstractSearchCondition{

    @Override
    public BooleanExpression apply(String searchWord) {
        long productGroupId = Long.parseLong(searchWord);
        return productGroup.id.gt(productGroupId);
    }

    @Override
    public SearchKeyword getSearchKeyword() {
        return EXCEL_PRODUCT_GROUP_ID;
    }
}
