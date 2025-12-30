package com.connectly.partnerAdmin.module.common.strategy.search;

import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.connectly.partnerAdmin.module.common.enums.SearchKeyword.PRODUCT_GROUP_ID;
import static com.connectly.partnerAdmin.module.product.entity.group.QProductGroup.productGroup;


@Component
public class ProductGroupIdSearchCondition extends AbstractSearchCondition{
    @Override
    public BooleanExpression apply(String searchWord) {
        List<Long> productGroupIds = splitWords(searchWord);
        return productGroup.id.in(productGroupIds);
    }

    @Override
    public BooleanExpression apply(String searchWord, Path<?> path) {
        List<Long> productGroupIds = splitWords(searchWord);
        PathBuilder<Object> entityPath = new PathBuilder<>(path.getType(), path.getMetadata());
        return entityPath.get("id", Long.class).in(productGroupIds);
    }

    @Override
    public SearchKeyword getSearchKeyword() {
        return PRODUCT_GROUP_ID;
    }
}
