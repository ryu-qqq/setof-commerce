package com.ryuqq.setof.adapter.out.persistence.content.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.content.entity.ContentJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.content.entity.QContentJpaEntity;
import com.ryuqq.setof.domain.cms.query.criteria.ContentSearchCriteria;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * ContentQueryDslRepository - Content QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class ContentQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QContentJpaEntity qContent = QContentJpaEntity.contentJpaEntity;

    public ContentQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /** ID로 Content 단건 조회 */
    public Optional<ContentJpaEntity> findById(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qContent)
                        .where(qContent.id.eq(id), qContent.deletedAt.isNull())
                        .fetchOne());
    }

    /** 검색 조건으로 Content 목록 조회 */
    public List<ContentJpaEntity> findByCriteria(ContentSearchCriteria criteria) {
        return queryFactory
                .selectFrom(qContent)
                .where(
                        titleContains(criteria.title()),
                        statusEquals(criteria.status() != null ? criteria.status().name() : null),
                        displayableAt(criteria.displayableAt()),
                        qContent.deletedAt.isNull())
                .orderBy(qContent.createdAt.desc())
                .offset(criteria.offset())
                .limit(criteria.limit())
                .fetch();
    }

    /** 검색 조건에 맞는 총 개수 조회 */
    public long countByCriteria(ContentSearchCriteria criteria) {
        Long count =
                queryFactory
                        .select(qContent.count())
                        .from(qContent)
                        .where(
                                titleContains(criteria.title()),
                                statusEquals(
                                        criteria.status() != null
                                                ? criteria.status().name()
                                                : null),
                                displayableAt(criteria.displayableAt()),
                                qContent.deletedAt.isNull())
                        .fetchOne();
        return count != null ? count : 0L;
    }

    /** ID로 존재 여부 확인 */
    public boolean existsById(Long id) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(qContent)
                        .where(qContent.id.eq(id), qContent.deletedAt.isNull())
                        .fetchFirst();
        return count != null;
    }

    // ===== Dynamic Conditions =====

    private BooleanExpression titleContains(String title) {
        if (title == null || title.isBlank()) {
            return null;
        }
        return qContent.title.containsIgnoreCase(title);
    }

    private BooleanExpression statusEquals(String status) {
        if (status == null) {
            return null;
        }
        return qContent.status.eq(status);
    }

    private BooleanExpression displayableAt(Instant displayableAt) {
        if (displayableAt == null) {
            return null;
        }
        return qContent.displayStartDate
                .loe(displayableAt)
                .and(qContent.displayEndDate.goe(displayableAt));
    }
}
