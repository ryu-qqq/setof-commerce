package com.connectly.partnerAdmin.module.common.strategy.search;

import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import static com.connectly.partnerAdmin.module.common.enums.SearchKeyword.BUYER_NAME;
import static com.connectly.partnerAdmin.module.payment.entity.QPaymentBill.paymentBill;


@Component
public class BuyerNameSearchCondition extends AbstractSearchCondition{
    @Override
    public BooleanExpression apply(String searchWord) {
        return paymentBill.buyerInfo.buyerName.like("%" + searchWord + "%");
    }

    @Override
    public SearchKeyword getSearchKeyword() {
        return BUYER_NAME;
    }
}
