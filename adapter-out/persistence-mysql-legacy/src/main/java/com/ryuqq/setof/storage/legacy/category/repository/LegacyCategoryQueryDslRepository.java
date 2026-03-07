package com.ryuqq.setof.storage.legacy.category.repository;

import static com.ryuqq.setof.storage.legacy.category.entity.QLegacyCategoryEntity.legacyCategoryEntity;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.sql.JPASQLQuery;
import com.querydsl.sql.SQLExpressions;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.Union;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.category.query.CategorySearchCriteria;
import com.ryuqq.setof.domain.category.query.CategorySortKey;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.storage.legacy.category.condition.LegacyCategoryConditionBuilder;
import com.ryuqq.setof.storage.legacy.category.dto.LegacyCategoryTreeDto;
import com.ryuqq.setof.storage.legacy.category.entity.LegacyCategoryEntity;
import com.ryuqq.setof.storage.legacy.category.entity.QLegacyCategoryEntity;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * LegacyCategoryQueryDslRepository - 레거시 카테고리 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>레거시 DB는 deletedAt 컬럼이 없으므로 Soft Delete 조건을 적용하지 않습니다.
 *
 * <p>트리 조회는 Recursive CTE를 사용하여 부모 카테고리를 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyCategoryQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;
    private final LegacyCategoryConditionBuilder conditionBuilder;

    public LegacyCategoryQueryDslRepository(
            JPAQueryFactory queryFactory,
            EntityManager entityManager,
            LegacyCategoryConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.entityManager = entityManager;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * ID로 카테고리 조회.
     *
     * @param id 카테고리 ID
     * @return 카테고리 Optional
     */
    public Optional<LegacyCategoryEntity> findById(Long id) {
        LegacyCategoryEntity entity =
                queryFactory
                        .selectFrom(legacyCategoryEntity)
                        .where(conditionBuilder.idEq(id))
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * ID 목록으로 카테고리 목록 조회.
     *
     * @param ids 카테고리 ID 목록
     * @return 카테고리 목록
     */
    public List<LegacyCategoryEntity> findByIds(List<Long> ids) {
        return queryFactory
                .selectFrom(legacyCategoryEntity)
                .where(conditionBuilder.idIn(ids))
                .fetch();
    }

    /**
     * ID 존재 여부 확인.
     *
     * @param id 카테고리 ID
     * @return 존재 여부
     */
    public boolean existsById(Long id) {
        Integer result =
                queryFactory
                        .selectOne()
                        .from(legacyCategoryEntity)
                        .where(conditionBuilder.idEq(id))
                        .fetchFirst();
        return result != null;
    }

    /**
     * 검색 조건으로 카테고리 목록 조회.
     *
     * @param criteria 검색 조건
     * @return 카테고리 목록
     */
    public List<LegacyCategoryEntity> findByCriteria(CategorySearchCriteria criteria) {
        QueryContext<CategorySortKey> qc = criteria.queryContext();

        return queryFactory
                .selectFrom(legacyCategoryEntity)
                .where(
                        conditionBuilder.parentCategoryIdEq(criteria.parentCategoryId()),
                        conditionBuilder.displayedEq(criteria.displayed()),
                        conditionBuilder.targetGroupEq(criteria.targetGroup()),
                        conditionBuilder.categoryTypeEq(criteria.categoryType()),
                        conditionBuilder.searchCondition(criteria))
                .orderBy(createOrderSpecifier(qc.sortKey(), qc.sortDirection()))
                .offset(criteria.offset())
                .limit(criteria.size())
                .fetch();
    }

    /**
     * 검색 조건으로 카테고리 개수 조회.
     *
     * @param criteria 검색 조건
     * @return 카테고리 개수
     */
    public long countByCriteria(CategorySearchCriteria criteria) {
        Long count =
                queryFactory
                        .select(legacyCategoryEntity.count())
                        .from(legacyCategoryEntity)
                        .where(
                                conditionBuilder.parentCategoryIdEq(criteria.parentCategoryId()),
                                conditionBuilder.displayedEq(criteria.displayed()),
                                conditionBuilder.targetGroupEq(criteria.targetGroup()),
                                conditionBuilder.categoryTypeEq(criteria.categoryType()),
                                conditionBuilder.searchCondition(criteria))
                        .fetchOne();
        return count != null ? count : 0L;
    }

    /**
     * 노출 중인 카테고리 전체 조회.
     *
     * @return 노출 중인 카테고리 목록
     */
    public List<LegacyCategoryEntity> findAllDisplayed() {
        return queryFactory
                .selectFrom(legacyCategoryEntity)
                .where(conditionBuilder.displayedEq(true))
                .orderBy(legacyCategoryEntity.categoryDepth.asc(), legacyCategoryEntity.id.asc())
                .fetch();
    }

    /**
     * 부모 카테고리 ID로 자식 카테고리 조회.
     *
     * @param parentId 부모 카테고리 ID
     * @return 자식 카테고리 목록
     */
    public List<LegacyCategoryEntity> findChildrenByParentId(Long parentId) {
        return queryFactory
                .selectFrom(legacyCategoryEntity)
                .where(conditionBuilder.parentCategoryIdEq(CategoryId.of(parentId)))
                .orderBy(legacyCategoryEntity.displayName.asc())
                .fetch();
    }

    /**
     * 자식 카테고리 ID로 모든 부모 카테고리 조회 (Recursive CTE).
     *
     * <p>Recursive CTE를 사용하여 주어진 카테고리의 모든 부모 카테고리를 조회합니다.
     *
     * @param childId 자식 카테고리 ID
     * @return 부모 카테고리 목록 (depth 오름차순, 루트부터)
     */
    public List<LegacyCategoryTreeDto> findParentsByChildId(Long childId) {
        JPASQLQuery<LegacyCategoryEntity> sqlQuery =
                new JPASQLQuery<>(entityManager, SQLTemplates.DEFAULT);

        QLegacyCategoryEntity c = new QLegacyCategoryEntity("c");
        QLegacyCategoryEntity sub = new QLegacyCategoryEntity("sub");
        EntityPathBase<QLegacyCategoryEntity> rec =
                new EntityPathBase<>(QLegacyCategoryEntity.class, "sub");

        SubQueryExpression<LegacyCategoryEntity> baseQuery =
                JPAExpressions.select(
                                Projections.fields(
                                        LegacyCategoryEntity.class,
                                        c.id,
                                        c.categoryName,
                                        c.categoryDepth,
                                        c.parentCategoryId,
                                        c.displayName,
                                        c.displayYn,
                                        c.targetGroup,
                                        c.categoryType,
                                        c.path,
                                        c.insertDate,
                                        c.updateDate))
                        .from(c)
                        .where(c.id.eq(childId));

        SubQueryExpression<LegacyCategoryEntity> recursiveQuery =
                JPAExpressions.select(
                                Projections.fields(
                                        LegacyCategoryEntity.class,
                                        c.id,
                                        c.categoryName,
                                        c.categoryDepth,
                                        c.parentCategoryId,
                                        c.displayName,
                                        c.displayYn,
                                        c.targetGroup,
                                        c.categoryType,
                                        c.path,
                                        c.insertDate,
                                        c.updateDate))
                        .from(rec)
                        .innerJoin(c)
                        .on(sub.parentCategoryId.eq(c.id));

        Union<LegacyCategoryEntity> union = SQLExpressions.unionAll(baseQuery, recursiveQuery);

        return sqlQuery.withRecursive(
                        rec,
                        c.id,
                        c.categoryName,
                        c.categoryDepth,
                        c.parentCategoryId,
                        c.displayName,
                        c.displayYn,
                        c.targetGroup,
                        c.categoryType,
                        c.path,
                        c.insertDate,
                        c.updateDate)
                .as(union)
                .select(
                        Projections.constructor(
                                LegacyCategoryTreeDto.class,
                                sub.id,
                                sub.categoryName,
                                sub.categoryDepth,
                                sub.parentCategoryId,
                                sub.displayName,
                                sub.displayYn,
                                sub.targetGroup,
                                sub.categoryType,
                                sub.path,
                                sub.insertDate,
                                sub.updateDate))
                .from(rec)
                .orderBy(sub.categoryDepth.asc())
                .fetch();
    }

    private OrderSpecifier<?> createOrderSpecifier(
            CategorySortKey sortKey, SortDirection direction) {
        boolean isAsc = direction == SortDirection.ASC;

        return switch (sortKey) {
            case CREATED_AT ->
                    isAsc
                            ? legacyCategoryEntity.insertDate.asc()
                            : legacyCategoryEntity.insertDate.desc();
            case CATEGORY_NAME ->
                    isAsc
                            ? legacyCategoryEntity.categoryName.asc()
                            : legacyCategoryEntity.categoryName.desc();
            case CATEGORY_DEPTH ->
                    isAsc
                            ? legacyCategoryEntity.categoryDepth.asc()
                            : legacyCategoryEntity.categoryDepth.desc();
            case DISPLAY_NAME ->
                    isAsc
                            ? legacyCategoryEntity.displayName.asc()
                            : legacyCategoryEntity.displayName.desc();
        };
    }
}
