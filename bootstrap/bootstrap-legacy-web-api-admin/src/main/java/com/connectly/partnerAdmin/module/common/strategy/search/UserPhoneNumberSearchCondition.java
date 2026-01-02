package com.connectly.partnerAdmin.module.common.strategy.search;

import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import static com.connectly.partnerAdmin.module.user.entity.QUsers.users;

@Component
public class UserPhoneNumberSearchCondition extends AbstractSearchCondition{

    @Override
    public BooleanExpression apply(String searchWord) {
        return users.phoneNumber.eq(searchWord);
    }

    @Override
    public SearchKeyword getSearchKeyword() {
        return SearchKeyword.MEMBER_PHONE_NUMBER;
    }
}
