package com.connectly.partnerAdmin.module.common.strategy.search;

import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import static com.connectly.partnerAdmin.module.common.enums.SearchKeyword.MEMBER_ID;
import static com.connectly.partnerAdmin.module.discount.entity.history.QDiscountUseHistory.discountUseHistory;

@Component
public class DiscountUseMemberIdSearchCondition extends AbstractSearchCondition{

    @Override
    public BooleanExpression apply(String searchWord) {
        long userId = Long.parseLong(searchWord);
        return discountUseHistory.userId.eq(userId);
    }

    @Override
    public SearchKeyword getSearchKeyword() {
        return MEMBER_ID;
    }
}
