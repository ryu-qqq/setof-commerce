package com.setof.connectly.module.news.repository.faq;

import static com.setof.connectly.module.news.entity.faq.QFaq.faq;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.news.dto.faq.FaqDto;
import com.setof.connectly.module.news.dto.faq.QFaqDto;
import com.setof.connectly.module.news.dto.faq.filter.FaqFilter;
import com.setof.connectly.module.news.enums.FaqType;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FaqFindRepositoryImpl implements FaqFindRepository {

    private final JPAQueryFactory queryFactory;

    public List<FaqDto> fetchFaq(FaqFilter filter) {
        List<OrderSpecifier<?>> orders = createOrderByFaqType(filter.getFaqType());
        return queryFactory
                .select(new QFaqDto(faq.faqType, faq.title, faq.contents))
                .from(faq)
                .where(faqTypeEq(filter.getFaqType()), hasTopDisplayOrder(filter.getFaqType()))
                .orderBy(orders.toArray(OrderSpecifier[]::new))
                .fetch();
    }

    private BooleanExpression faqTypeEq(FaqType faqType) {
        if (faqType.isTop()) return null;
        return faq.faqType.eq(faqType);
    }

    private BooleanExpression hasTopDisplayOrder(FaqType faqType) {
        if (faqType.isTop()) return faq.topDisplayOrder.isNotNull();
        else return null;
    }

    private List<OrderSpecifier<?>> createOrderByFaqType(FaqType faqType) {
        List<OrderSpecifier<?>> orderSpecifiers = new LinkedList<>();

        if (faqType.isTop()) {
            orderSpecifiers.add(faq.topDisplayOrder.asc());
        } else {
            orderSpecifiers.add(faq.displayOrder.asc());
        }
        return orderSpecifiers;
    }
}
