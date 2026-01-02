package com.connectly.partnerAdmin.module.common.strategy.search;


import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.connectly.partnerAdmin.module.common.enums.SearchKeyword.ORDER_ID;
import static com.connectly.partnerAdmin.module.common.enums.SearchKeyword.PAYMENT_ID;
import static com.connectly.partnerAdmin.module.payment.entity.QPayment.payment;


@Component
public class PaymentIdSearchCondition extends AbstractSearchCondition{

    @Override
    public BooleanExpression apply(String searchWord) {
        List<Long> paymentIds = splitWords(searchWord);
        return payment.id.in(paymentIds);
    }

    @Override
    public SearchKeyword getSearchKeyword() {
        return PAYMENT_ID;
    }
}
