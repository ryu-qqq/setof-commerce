package com.ryuqq.setof.adapter.out.persistence.faq.repository;

import static com.ryuqq.setof.adapter.out.persistence.faq.entity.QFaqJpaEntity.faqJpaEntity;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.faq.condition.FaqConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.faq.entity.FaqJpaEntity;
import com.ryuqq.setof.domain.faq.query.FaqSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * FaqQueryDslRepository - FAQ QueryDSL 레포지토리.
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
public class FaqQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final FaqConditionBuilder conditionBuilder;

    public FaqQueryDslRepository(
            JPAQueryFactory queryFactory, FaqConditionBuilder conditionBuilder) {
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
    public List<FaqJpaEntity> findByCriteria(FaqSearchCriteria criteria) {
        return queryFactory
                .selectFrom(faqJpaEntity)
                .where(
                        conditionBuilder.faqTypeEq(criteria.faqType()),
                        conditionBuilder.notDeleted())
                .orderBy(resolveOrderSpecifier(criteria))
                .fetch();
    }

    private OrderSpecifier<?> resolveOrderSpecifier(FaqSearchCriteria criteria) {
        if (criteria.isTop()) {
            return faqJpaEntity.topDisplayOrder.asc();
        }
        return faqJpaEntity.displayOrder.asc();
    }
}
