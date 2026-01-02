package com.connectly.partnerAdmin.module.common.strategy.search;


import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import static com.connectly.partnerAdmin.module.common.enums.SearchKeyword.SELLER_ID;
import static com.connectly.partnerAdmin.module.common.enums.SearchKeyword.SELLER_NAME;
import static com.connectly.partnerAdmin.module.seller.entity.QSeller.seller;


@Component
public class SellerIdSearchCondition extends AbstractSearchCondition{

    @Override
    public BooleanExpression apply(String searchWord) {
        long sellerId = Long.parseLong(searchWord);
        return seller.id.eq(sellerId);
    }

    @Override
    public SearchKeyword getSearchKeyword() {
        return SELLER_ID;
    }
}
