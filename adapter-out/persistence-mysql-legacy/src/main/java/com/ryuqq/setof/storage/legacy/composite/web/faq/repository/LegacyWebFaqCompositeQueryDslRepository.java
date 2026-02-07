package com.ryuqq.setof.storage.legacy.composite.web.faq.repository;

import static com.ryuqq.setof.storage.legacy.faq.entity.QLegacyFaqEntity.legacyFaqEntity;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.legacy.faq.FaqType;
import com.ryuqq.setof.domain.legacy.faq.dto.query.LegacyFaqSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.faq.condition.LegacyWebFaqCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.web.faq.dto.LegacyWebFaqQueryDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyWebFaqCompositeQueryDslRepository - 레거시 Web FAQ Composite 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>Projections.constructor() 사용 (@QueryProjection 금지).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyWebFaqCompositeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebFaqCompositeConditionBuilder conditionBuilder;

    public LegacyWebFaqCompositeQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebFaqCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * FAQ 목록 조회.
     *
     * <p>TOP 유형: topDisplayOrder가 있는 항목만 조회, topDisplayOrder ASC 정렬.
     *
     * <p>일반 유형: 해당 유형만 조회, displayOrder ASC 정렬.
     *
     * @param condition 검색 조건
     * @return FAQ 목록
     */
    public List<LegacyWebFaqQueryDto> fetchFaqs(LegacyFaqSearchCondition condition) {
        List<OrderSpecifier<?>> orders = createOrderBy(condition.faqType());

        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebFaqQueryDto.class,
                                legacyFaqEntity.faqType,
                                legacyFaqEntity.title,
                                legacyFaqEntity.contents))
                .from(legacyFaqEntity)
                .where(
                        conditionBuilder.faqTypeEq(condition.faqType()),
                        conditionBuilder.hasTopDisplayOrder(condition.faqType()))
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .fetch();
    }

    /**
     * FAQ 유형에 따른 정렬 조건 생성.
     *
     * @param faqType FAQ 유형
     * @return 정렬 조건 목록
     */
    private List<OrderSpecifier<?>> createOrderBy(FaqType faqType) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (faqType == FaqType.TOP) {
            orders.add(legacyFaqEntity.topDisplayOrder.asc());
        } else {
            orders.add(legacyFaqEntity.displayOrder.asc());
        }

        return orders;
    }
}
