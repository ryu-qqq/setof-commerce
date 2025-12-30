package com.connectly.partnerAdmin.module.common.strategy.search;

import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;

public interface SearchCondition {

    BooleanExpression apply(String searchWord);
    BooleanExpression apply(String searchWord, Path<?> path);
    SearchKeyword getSearchKeyword();
}
