package com.ryuqq.setof.adapter.out.persistence.faqcategory.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.faqcategory.entity.FaqCategoryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.faqcategory.entity.QFaqCategoryJpaEntity;
import com.ryuqq.setof.domain.faq.query.FaqCategorySearchCriteria;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * FaqCategoryQueryDslRepository - FAQ 카테고리 QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class FaqCategoryQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QFaqCategoryJpaEntity qFaqCategory =
            QFaqCategoryJpaEntity.faqCategoryJpaEntity;

    public FaqCategoryQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /** ID로 FAQ 카테고리 단건 조회 */
    public Optional<FaqCategoryJpaEntity> findById(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qFaqCategory)
                        .where(qFaqCategory.id.eq(id), qFaqCategory.deletedAt.isNull())
                        .fetchOne());
    }

    /** 코드로 FAQ 카테고리 단건 조회 */
    public Optional<FaqCategoryJpaEntity> findByCode(String code) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qFaqCategory)
                        .where(qFaqCategory.code.eq(code), qFaqCategory.deletedAt.isNull())
                        .fetchOne());
    }

    /** 검색 조건으로 FAQ 카테고리 목록 조회 */
    public List<FaqCategoryJpaEntity> findByCriteria(FaqCategorySearchCriteria criteria) {
        return queryFactory
                .selectFrom(qFaqCategory)
                .where(
                        statusEquals(criteria.status() != null ? criteria.status().name() : null),
                        qFaqCategory.deletedAt.isNull())
                .orderBy(qFaqCategory.displayOrder.asc(), qFaqCategory.createdAt.desc())
                .offset(criteria.offset())
                .limit(criteria.limit())
                .fetch();
    }

    /** 검색 조건으로 FAQ 카테고리 개수 조회 */
    public long countByCriteria(FaqCategorySearchCriteria criteria) {
        Long count =
                queryFactory
                        .select(qFaqCategory.count())
                        .from(qFaqCategory)
                        .where(
                                statusEquals(
                                        criteria.status() != null
                                                ? criteria.status().name()
                                                : null),
                                qFaqCategory.deletedAt.isNull())
                        .fetchOne();
        return count != null ? count : 0L;
    }

    /** ID로 존재 여부 확인 */
    public boolean existsById(Long id) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(qFaqCategory)
                        .where(qFaqCategory.id.eq(id), qFaqCategory.deletedAt.isNull())
                        .fetchFirst();
        return count != null;
    }

    /** 코드로 존재 여부 확인 */
    public boolean existsByCode(String code) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(qFaqCategory)
                        .where(qFaqCategory.code.eq(code), qFaqCategory.deletedAt.isNull())
                        .fetchFirst();
        return count != null;
    }

    // ===== Dynamic Conditions =====

    private BooleanExpression statusEquals(String status) {
        if (status == null) {
            return null;
        }
        return qFaqCategory.status.eq(status);
    }
}
