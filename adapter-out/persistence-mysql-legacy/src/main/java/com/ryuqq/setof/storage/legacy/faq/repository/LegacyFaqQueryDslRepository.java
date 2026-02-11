package com.ryuqq.setof.storage.legacy.faq.repository;

import static com.ryuqq.setof.storage.legacy.faq.entity.QLegacyFaqEntity.legacyFaqEntity;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.faq.query.FaqSearchCriteria;
import com.ryuqq.setof.storage.legacy.faq.condition.LegacyFaqConditionBuilder;
import com.ryuqq.setof.storage.legacy.faq.entity.LegacyFaqEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * LegacyFaqQueryDslRepository - 레거시 FAQ QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>정렬 규칙:
 *
 * <ul>
 *   <li>TOP 유형: topDisplayOrder ASC
 *   <li>그 외: displayOrder ASC
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyFaqQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyFaqConditionBuilder conditionBuilder;

    public LegacyFaqQueryDslRepository(
            JPAQueryFactory queryFactory, LegacyFaqConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 검색 조건으로 FAQ 목록 조회.
     *
     * <p>faqType에 따라 정렬 기준이 달라집니다.
     *
     * @param criteria 검색 조건
     * @return FAQ 목록
     */
    public List<LegacyFaqEntity> findByCriteria(FaqSearchCriteria criteria) {
        return queryFactory
                .selectFrom(legacyFaqEntity)
                .where(conditionBuilder.faqTypeEq(criteria.faqType()))
                .orderBy(resolveOrderSpecifier(criteria))
                .fetch();
    }

    private OrderSpecifier<?> resolveOrderSpecifier(FaqSearchCriteria criteria) {
        if (criteria.isTop()) {
            return legacyFaqEntity.topDisplayOrder.asc();
        }
        return legacyFaqEntity.displayOrder.asc();
    }
}
