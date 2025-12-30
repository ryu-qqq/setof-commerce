package com.connectly.partnerAdmin.module.common.strategy.search;

import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import static com.connectly.partnerAdmin.module.common.enums.SearchKeyword.QUESTIONER_NAME;
import static com.connectly.partnerAdmin.module.user.entity.QUsers.users;

@Component
public class QuestionNameSearchCondition extends AbstractSearchCondition{
    @Override
    public BooleanExpression apply(String searchWord) {
        return users.name.like("%" + searchWord + "%");
    }

    @Override
    public SearchKeyword getSearchKeyword() {
        return QUESTIONER_NAME;
    }
}
