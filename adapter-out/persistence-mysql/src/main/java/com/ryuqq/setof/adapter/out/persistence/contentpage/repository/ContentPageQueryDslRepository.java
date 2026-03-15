package com.ryuqq.setof.adapter.out.persistence.contentpage.repository;

import static com.ryuqq.setof.adapter.out.persistence.contentpage.entity.QContentPageJpaEntity.contentPageJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.contentpage.condition.ContentPageConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.contentpage.entity.ContentPageJpaEntity;
import com.ryuqq.setof.domain.contentpage.query.ContentPageListSearchCriteria;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ContentPageQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final ContentPageConditionBuilder conditionBuilder;

    public ContentPageQueryDslRepository(
            JPAQueryFactory queryFactory, ContentPageConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    public Optional<ContentPageJpaEntity> fetchById(long id) {
        ContentPageJpaEntity entity =
                queryFactory
                        .selectFrom(contentPageJpaEntity)
                        .where(
                                conditionBuilder.contentPageIdEq(id),
                                conditionBuilder.contentPageNotDeleted())
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    public Optional<ContentPageJpaEntity> fetchByIdWithBypass(long id, boolean bypass) {
        ContentPageJpaEntity entity =
                queryFactory
                        .selectFrom(contentPageJpaEntity)
                        .where(
                                conditionBuilder.contentPageIdEq(id),
                                bypass ? null : conditionBuilder.contentPageActiveEq(true),
                                conditionBuilder.contentPageNotDeleted())
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    public List<Long> fetchOnDisplayContentPageIds() {
        return queryFactory
                .select(contentPageJpaEntity.id)
                .from(contentPageJpaEntity)
                .where(
                        conditionBuilder.contentPageActiveEq(true),
                        conditionBuilder.contentPageNotDeleted(),
                        conditionBuilder.contentPageDisplayPeriodBetween())
                .fetch();
    }

    public List<ContentPageJpaEntity> searchContentPages(ContentPageListSearchCriteria criteria) {
        var query =
                queryFactory
                        .selectFrom(contentPageJpaEntity)
                        .where(
                                conditionBuilder.contentPageNotDeleted(),
                                conditionBuilder.contentPageActiveEq(criteria.active()),
                                conditionBuilder.contentPageDisplayStartAfter(
                                        criteria.displayPeriodStart()),
                                conditionBuilder.contentPageDisplayEndBefore(
                                        criteria.displayPeriodEnd()),
                                conditionBuilder.contentPageCreatedAtAfter(
                                        criteria.createdAtStart()),
                                conditionBuilder.contentPageCreatedAtBefore(
                                        criteria.createdAtEnd()),
                                conditionBuilder.contentPageTitleContains(
                                        criteria.titleKeyword()),
                                conditionBuilder.contentPageIdEq(criteria.contentPageId()),
                                conditionBuilder.contentPageIdLt(criteria.lastDomainId()))
                        .orderBy(contentPageJpaEntity.id.desc());

        if (criteria.isNoOffset()) {
            query.limit(criteria.size());
        } else {
            query.offset(criteria.offset()).limit(criteria.size());
        }

        return query.fetch();
    }

    public long countContentPages(ContentPageListSearchCriteria criteria) {
        Long count =
                queryFactory
                        .select(contentPageJpaEntity.count())
                        .from(contentPageJpaEntity)
                        .where(
                                conditionBuilder.contentPageNotDeleted(),
                                conditionBuilder.contentPageActiveEq(criteria.active()),
                                conditionBuilder.contentPageDisplayStartAfter(
                                        criteria.displayPeriodStart()),
                                conditionBuilder.contentPageDisplayEndBefore(
                                        criteria.displayPeriodEnd()),
                                conditionBuilder.contentPageCreatedAtAfter(
                                        criteria.createdAtStart()),
                                conditionBuilder.contentPageCreatedAtBefore(
                                        criteria.createdAtEnd()),
                                conditionBuilder.contentPageTitleContains(
                                        criteria.titleKeyword()),
                                conditionBuilder.contentPageIdEq(criteria.contentPageId()))
                        .fetchOne();
        return count != null ? count : 0L;
    }
}
