package com.ryuqq.setof.adapter.out.persistence.category.repository;

import static com.ryuqq.setof.adapter.out.persistence.category.entity.QCategoryJpaEntity.categoryJpaEntity;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.sql.JPASQLQuery;
import com.querydsl.sql.SQLExpressions;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.Union;
import com.ryuqq.setof.adapter.out.persistence.category.condition.CategoryConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.category.dto.CategoryTreeDto;
import com.ryuqq.setof.adapter.out.persistence.category.entity.CategoryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.category.entity.QCategoryJpaEntity;
import com.ryuqq.setof.domain.category.query.CategorySearchCriteria;
import com.ryuqq.setof.domain.category.query.CategorySortKey;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * CategoryQueryDslRepository - 카테고리 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>트리 조회는 Recursive CTE를 사용하여 자식/부모 카테고리를 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Repository
public class CategoryQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;
    private final CategoryConditionBuilder conditionBuilder;

    public CategoryQueryDslRepository(
            JPAQueryFactory queryFactory,
            EntityManager entityManager,
            CategoryConditionBuilder conditionBuilder) {
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
    public Optional<CategoryJpaEntity> findById(Long id) {
        CategoryJpaEntity entity =
                queryFactory
                        .selectFrom(categoryJpaEntity)
                        .where(conditionBuilder.idEq(id), conditionBuilder.notDeleted())
                        .fetchOne();
        return Optional.ofNullable(entity);
    }

    /**
     * ID 목록으로 카테고리 목록 조회.
     *
     * @param ids 카테고리 ID 목록
     * @return 카테고리 목록
     */
    public List<CategoryJpaEntity> findByIds(List<Long> ids) {
        return queryFactory
                .selectFrom(categoryJpaEntity)
                .where(conditionBuilder.idIn(ids), conditionBuilder.notDeleted())
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
                        .from(categoryJpaEntity)
                        .where(conditionBuilder.idEq(id), conditionBuilder.notDeleted())
                        .fetchFirst();
        return result != null;
    }

    /**
     * 검색 조건으로 카테고리 목록 조회.
     *
     * @param criteria 검색 조건
     * @return 카테고리 목록
     */
    public List<CategoryJpaEntity> findByCriteria(CategorySearchCriteria criteria) {
        QueryContext<CategorySortKey> qc = criteria.queryContext();

        Long parentIdValue = criteria.parentCategoryIdValue();

        return queryFactory
                .selectFrom(categoryJpaEntity)
                .where(
                        conditionBuilder.parentCategoryIdEq(parentIdValue),
                        conditionBuilder.displayedEq(criteria.displayed()),
                        conditionBuilder.targetGroupEq(criteria.targetGroup()),
                        conditionBuilder.categoryTypeEq(criteria.categoryType()),
                        conditionBuilder.searchCondition(criteria),
                        conditionBuilder.notDeleted())
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
        Long parentIdValue = criteria.parentCategoryIdValue();

        Long count =
                queryFactory
                        .select(categoryJpaEntity.count())
                        .from(categoryJpaEntity)
                        .where(
                                conditionBuilder.parentCategoryIdEq(parentIdValue),
                                conditionBuilder.displayedEq(criteria.displayed()),
                                conditionBuilder.targetGroupEq(criteria.targetGroup()),
                                conditionBuilder.categoryTypeEq(criteria.categoryType()),
                                conditionBuilder.searchCondition(criteria),
                                conditionBuilder.notDeleted())
                        .fetchOne();
        return count != null ? count : 0L;
    }

    /**
     * 노출 중인 카테고리 전체 조회.
     *
     * @return 노출 중인 카테고리 목록
     */
    public List<CategoryJpaEntity> findAllDisplayed() {
        return queryFactory
                .selectFrom(categoryJpaEntity)
                .where(conditionBuilder.displayedEq(true), conditionBuilder.notDeleted())
                .orderBy(categoryJpaEntity.categoryDepth.asc(), categoryJpaEntity.id.asc())
                .fetch();
    }

    /**
     * 부모 카테고리 ID로 직계 자식 카테고리 목록 조회.
     *
     * @param parentId 부모 카테고리 ID
     * @return 자식 카테고리 목록
     */
    public List<CategoryJpaEntity> findChildrenByParentId(Long parentId) {
        return queryFactory
                .selectFrom(categoryJpaEntity)
                .where(conditionBuilder.parentCategoryIdEq(parentId), conditionBuilder.notDeleted())
                .orderBy(categoryJpaEntity.id.asc())
                .fetch();
    }

    /**
     * 부모 카테고리 ID로 모든 하위 카테고리 조회 (Recursive CTE).
     *
     * <p>Recursive CTE를 사용하여 주어진 카테고리의 모든 하위 카테고리를 조회합니다.
     *
     * @param categoryId 부모 카테고리 ID
     * @return 하위 카테고리 목록 (depth 내림차순)
     */
    public List<CategoryTreeDto> findDescendantsByCategoryId(Long categoryId) {
        JPASQLQuery<CategoryJpaEntity> sqlQuery =
                new JPASQLQuery<>(entityManager, SQLTemplates.DEFAULT);

        QCategoryJpaEntity c = new QCategoryJpaEntity("c");
        QCategoryJpaEntity sub = new QCategoryJpaEntity("sub");
        EntityPathBase<QCategoryJpaEntity> rec =
                new EntityPathBase<>(QCategoryJpaEntity.class, "sub");

        JPQLQuery<CategoryJpaEntity> baseQuery =
                JPAExpressions.select(
                                Projections.fields(
                                        CategoryJpaEntity.class,
                                        c.id,
                                        c.categoryName,
                                        c.categoryDepth,
                                        c.parentCategoryId,
                                        c.displayName,
                                        c.displayed,
                                        c.targetGroup,
                                        c.categoryType,
                                        c.path,
                                        c.createdAt,
                                        c.updatedAt,
                                        c.deletedAt))
                        .from(c)
                        .where(c.id.eq(categoryId));

        JPQLQuery<CategoryJpaEntity> recursiveQuery =
                JPAExpressions.select(
                                Projections.fields(
                                        CategoryJpaEntity.class,
                                        c.id,
                                        c.categoryName,
                                        c.categoryDepth,
                                        c.parentCategoryId,
                                        c.displayName,
                                        c.displayed,
                                        c.targetGroup,
                                        c.categoryType,
                                        c.path,
                                        c.createdAt,
                                        c.updatedAt,
                                        c.deletedAt))
                        .from(rec)
                        .innerJoin(c)
                        .on(sub.id.eq(c.parentCategoryId));

        Union<CategoryJpaEntity> union = SQLExpressions.unionAll(baseQuery, recursiveQuery);

        return sqlQuery.withRecursive(
                        rec,
                        c.id,
                        c.categoryName,
                        c.categoryDepth,
                        c.parentCategoryId,
                        c.displayName,
                        c.displayed,
                        c.targetGroup,
                        c.categoryType,
                        c.path,
                        c.createdAt,
                        c.updatedAt,
                        c.deletedAt)
                .as(union)
                .select(
                        Projections.constructor(
                                CategoryTreeDto.class,
                                sub.id,
                                sub.categoryName,
                                sub.categoryDepth,
                                sub.parentCategoryId,
                                sub.displayName,
                                sub.displayed,
                                sub.targetGroup,
                                sub.categoryType,
                                sub.path,
                                sub.createdAt,
                                sub.updatedAt,
                                sub.deletedAt))
                .from(rec)
                .orderBy(sub.categoryDepth.desc())
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
    public List<CategoryTreeDto> findAncestorsByChildId(Long childId) {
        JPASQLQuery<CategoryJpaEntity> sqlQuery =
                new JPASQLQuery<>(entityManager, SQLTemplates.DEFAULT);

        QCategoryJpaEntity c = new QCategoryJpaEntity("c");
        QCategoryJpaEntity sub = new QCategoryJpaEntity("sub");
        EntityPathBase<QCategoryJpaEntity> rec =
                new EntityPathBase<>(QCategoryJpaEntity.class, "sub");

        SubQueryExpression<CategoryJpaEntity> baseQuery =
                JPAExpressions.select(
                                Projections.fields(
                                        CategoryJpaEntity.class,
                                        c.id,
                                        c.categoryName,
                                        c.categoryDepth,
                                        c.parentCategoryId,
                                        c.displayName,
                                        c.displayed,
                                        c.targetGroup,
                                        c.categoryType,
                                        c.path,
                                        c.createdAt,
                                        c.updatedAt,
                                        c.deletedAt))
                        .from(c)
                        .where(c.id.eq(childId));

        SubQueryExpression<CategoryJpaEntity> recursiveQuery =
                JPAExpressions.select(
                                Projections.fields(
                                        CategoryJpaEntity.class,
                                        c.id,
                                        c.categoryName,
                                        c.categoryDepth,
                                        c.parentCategoryId,
                                        c.displayName,
                                        c.displayed,
                                        c.targetGroup,
                                        c.categoryType,
                                        c.path,
                                        c.createdAt,
                                        c.updatedAt,
                                        c.deletedAt))
                        .from(rec)
                        .innerJoin(c)
                        .on(sub.parentCategoryId.eq(c.id));

        Union<CategoryJpaEntity> union = SQLExpressions.unionAll(baseQuery, recursiveQuery);

        return sqlQuery.withRecursive(
                        rec,
                        c.id,
                        c.categoryName,
                        c.categoryDepth,
                        c.parentCategoryId,
                        c.displayName,
                        c.displayed,
                        c.targetGroup,
                        c.categoryType,
                        c.path,
                        c.createdAt,
                        c.updatedAt,
                        c.deletedAt)
                .as(union)
                .select(
                        Projections.constructor(
                                CategoryTreeDto.class,
                                sub.id,
                                sub.categoryName,
                                sub.categoryDepth,
                                sub.parentCategoryId,
                                sub.displayName,
                                sub.displayed,
                                sub.targetGroup,
                                sub.categoryType,
                                sub.path,
                                sub.createdAt,
                                sub.updatedAt,
                                sub.deletedAt))
                .from(rec)
                .orderBy(sub.categoryDepth.asc())
                .fetch();
    }

    private OrderSpecifier<?> createOrderSpecifier(
            CategorySortKey sortKey, SortDirection direction) {
        boolean isAsc = direction == SortDirection.ASC;

        return switch (sortKey) {
            case CREATED_AT ->
                    isAsc ? categoryJpaEntity.createdAt.asc() : categoryJpaEntity.createdAt.desc();
            case CATEGORY_NAME ->
                    isAsc
                            ? categoryJpaEntity.categoryName.asc()
                            : categoryJpaEntity.categoryName.desc();
            case CATEGORY_DEPTH ->
                    isAsc
                            ? categoryJpaEntity.categoryDepth.asc()
                            : categoryJpaEntity.categoryDepth.desc();
            case DISPLAY_NAME ->
                    isAsc
                            ? categoryJpaEntity.displayName.asc()
                            : categoryJpaEntity.displayName.desc();
        };
    }
}
