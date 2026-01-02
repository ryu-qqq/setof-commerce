package com.ryuqq.setof.adapter.out.persistence.faq.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.faq.entity.FaqJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.faq.entity.QFaqJpaEntity;
import com.ryuqq.setof.domain.faq.query.FaqSearchCriteria;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * FaqQueryDslRepository - FAQ QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class FaqQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QFaqJpaEntity qFaq = QFaqJpaEntity.faqJpaEntity;

    public FaqQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /** ID로 FAQ 단건 조회 */
    public Optional<FaqJpaEntity> findById(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qFaq)
                        .where(qFaq.id.eq(id), qFaq.deletedAt.isNull())
                        .fetchOne());
    }

    /** 검색 조건으로 FAQ 목록 조회 */
    public List<FaqJpaEntity> findByCriteria(FaqSearchCriteria criteria) {
        return queryFactory
                .selectFrom(qFaq)
                .where(
                        categoryCodeEquals(
                                criteria.categoryCode() != null
                                        ? criteria.categoryCode().value()
                                        : null),
                        statusEquals(criteria.status() != null ? criteria.status().name() : null),
                        isTopEquals(criteria.isTop()),
                        qFaq.deletedAt.isNull())
                .orderBy(
                        qFaq.top.desc(),
                        qFaq.topOrder.asc(),
                        qFaq.displayOrder.asc(),
                        qFaq.createdAt.desc())
                .offset(criteria.offset())
                .limit(criteria.limit())
                .fetch();
    }

    /** 검색 조건으로 FAQ 개수 조회 */
    public long countByCriteria(FaqSearchCriteria criteria) {
        Long count =
                queryFactory
                        .select(qFaq.count())
                        .from(qFaq)
                        .where(
                                categoryCodeEquals(
                                        criteria.categoryCode() != null
                                                ? criteria.categoryCode().value()
                                                : null),
                                statusEquals(
                                        criteria.status() != null
                                                ? criteria.status().name()
                                                : null),
                                isTopEquals(criteria.isTop()),
                                qFaq.deletedAt.isNull())
                        .fetchOne();
        return count != null ? count : 0L;
    }

    /** ID로 존재 여부 확인 */
    public boolean existsById(Long id) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(qFaq)
                        .where(qFaq.id.eq(id), qFaq.deletedAt.isNull())
                        .fetchFirst();
        return count != null;
    }

    // ===== Dynamic Conditions =====

    private BooleanExpression categoryCodeEquals(String categoryCode) {
        if (categoryCode == null) {
            return null;
        }
        return qFaq.categoryCode.eq(categoryCode);
    }

    private BooleanExpression statusEquals(String status) {
        if (status == null) {
            return null;
        }
        return qFaq.status.eq(status);
    }

    private BooleanExpression isTopEquals(Boolean isTop) {
        if (isTop == null) {
            return null;
        }
        return qFaq.top.eq(isTop);
    }
}
