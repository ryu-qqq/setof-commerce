package com.connectly.partnerAdmin.module.common.repository;

import com.connectly.partnerAdmin.module.common.enums.SearchKeyword;
import com.connectly.partnerAdmin.module.common.enums.SortField;
import com.connectly.partnerAdmin.module.common.strategy.search.SearchCondition;
import com.connectly.partnerAdmin.module.common.strategy.search.SearchConditionStrategy;
import com.connectly.partnerAdmin.module.common.strategy.sort.SortCondition;
import com.connectly.partnerAdmin.module.common.strategy.sort.SortConditionStrategy;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Repository
public abstract class AbstractCommonRepository implements CommonRepository {

    private final JPAQueryFactory queryFactory;
    private final SearchConditionStrategy searchConditionStrategy;
    private final SortConditionStrategy sortConditionStrategy;

    protected AbstractCommonRepository(JPAQueryFactory queryFactory, SearchConditionStrategy searchConditionStrategy, SortConditionStrategy sortConditionStrategy) {
        this.queryFactory = queryFactory;
        this.searchConditionStrategy = searchConditionStrategy;
        this.sortConditionStrategy = sortConditionStrategy;
    }

    @Override
    public JPAQueryFactory getQueryFactory() {
        return this.queryFactory;
    }

    @Override
    public BooleanExpression searchKeywordEq(SearchKeyword searchKeyword, String searchWord) {
        if(!StringUtils.hasText(searchWord)) return null;
        SearchCondition searchCondition = searchConditionStrategy.get(searchKeyword);
        return searchCondition.apply(searchWord);
    }

    @Override
    public BooleanExpression searchKeywordEq(SearchKeyword searchKeyword, String searchWord, Path<?> path) {
        if(!StringUtils.hasText(searchWord)) return null;
        SearchCondition searchCondition = searchConditionStrategy.get(searchKeyword);
        return searchCondition.apply(searchWord, path);
    }

    @Override
    public List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                String property = order.getProperty();
                SortField sortField = SortField.ofDisplayName(property);
                if (sortField != null) {
                    Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                    SortCondition sortCondition = sortConditionStrategy.get(sortField);
                    OrderSpecifier<?> apply = sortCondition.apply(direction);
                    orders.add(apply);
                }
            }
        }
        return orders;
    }

    @Override
    public List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable, Path<?> path) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                String property = order.getProperty();
                SortField sortField = SortField.ofDisplayName(property);
                if (sortField != null) {
                    Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                    SortCondition sortCondition = sortConditionStrategy.get(sortField);
                    OrderSpecifier<?> apply = sortCondition.apply(direction, path);
                    orders.add(apply);

                }
            }
        }
        return orders;
    }

}
