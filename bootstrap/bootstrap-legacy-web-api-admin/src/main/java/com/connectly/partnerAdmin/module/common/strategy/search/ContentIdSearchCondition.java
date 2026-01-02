package com.connectly.partnerAdmin.module.common.strategy.search;


import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import com.connectly.partnerAdmin.module.display.entity.content.QContent;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.connectly.partnerAdmin.module.common.enums.SearchKeyword.CONTENT_ID;
import static com.connectly.partnerAdmin.module.display.entity.content.QContent.content;

@Component
public class ContentIdSearchCondition extends AbstractSearchCondition{

    @Override
    public BooleanExpression apply(String searchWord) {
        List<Long> contentIds = splitWords(searchWord);
        return content.id.in(contentIds);    }

    @Override
    public SearchKeyword getSearchKeyword() {
        return CONTENT_ID;
    }
}
