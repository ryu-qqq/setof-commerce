package com.connectly.partnerAdmin.module.common.repository;

import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommonRepository {
    JPAQueryFactory getQueryFactory();
    BooleanExpression searchKeywordEq(SearchKeyword searchKeyword, String searchWord);
    BooleanExpression searchKeywordEq(SearchKeyword searchKeyword, String searchWord, Path<?> path);

    List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable);
    List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable, Path<?> path);
}
