package com.connectly.partnerAdmin.module.utils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QueryDslUtil {

    public static <T extends Enum<T>> BooleanExpression notDeleted(EnumPath<T> deleteYn, T noValue) {
        return deleteYn.eq(noValue);
    }

}
