package com.connectly.partnerAdmin.module.common.strategy.search;

import java.util.List;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;

import static com.connectly.partnerAdmin.module.common.enums.SearchKeyword.ORDER_SNAPSHOT_PRODUCT_GROUP_ID;
import static com.connectly.partnerAdmin.module.order.entity.snapshot.group.QOrderSnapShotProductGroup.orderSnapShotProductGroup;

@Component
public class OrderSnapShotProductGroupIdSearchCondition extends AbstractSearchCondition{

    @Override
    public BooleanExpression apply(String searchWord) {
        List<Long> productGroupIds = splitWords(searchWord);
        return orderSnapShotProductGroup.snapShotProductGroup.productGroupId.in(productGroupIds);
    }

    @Override
    public BooleanExpression apply(String searchWord, Path<?> path) {
        List<Long> productGroupIds = splitWords(searchWord);
        return orderSnapShotProductGroup.snapShotProductGroup.productGroupId.in(productGroupIds);
    }

    @Override
    public SearchKeyword getSearchKeyword() {
        return ORDER_SNAPSHOT_PRODUCT_GROUP_ID;
    }
}
