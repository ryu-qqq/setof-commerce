package com.connectly.partnerAdmin.module.common.strategy.search;


import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import com.connectly.partnerAdmin.module.order.entity.QOrder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.connectly.partnerAdmin.module.common.enums.SearchKeyword.ORDER_ID;
import static com.connectly.partnerAdmin.module.order.entity.QOrder.order;


@Component
public class OrderIdSearchCondition extends AbstractSearchCondition{

    @Override
    public BooleanExpression apply(String searchWord) {
        List<Long> orderIds = splitWords(searchWord);
        return order.id.in(orderIds);
    }

    @Override
    public SearchKeyword getSearchKeyword() {
        return ORDER_ID;
    }
}
