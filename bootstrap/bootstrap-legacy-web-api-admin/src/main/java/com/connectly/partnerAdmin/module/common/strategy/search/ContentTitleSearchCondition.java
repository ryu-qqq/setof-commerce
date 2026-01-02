package com.connectly.partnerAdmin.module.common.strategy.search;


import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import com.connectly.partnerAdmin.module.display.entity.content.QContent;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import static com.connectly.partnerAdmin.module.common.enums.SearchKeyword.CONTENT_TITLE;
import static com.connectly.partnerAdmin.module.display.entity.content.QContent.content;

@Component
public class ContentTitleSearchCondition extends AbstractSearchCondition{

    @Override
    public BooleanExpression apply(String searchWord) {
        return content.title.like("%" + searchWord + "%");
    }

    @Override
    public SearchKeyword getSearchKeyword() {
        return CONTENT_TITLE;
    }
}
