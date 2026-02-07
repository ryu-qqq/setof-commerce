package com.ryuqq.setof.storage.legacy.composite.web.category.repository;

import static com.ryuqq.setof.storage.legacy.category.entity.QLegacyCategoryEntity.legacyCategoryEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.legacy.category.dto.query.LegacyCategorySearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.category.condition.LegacyWebCategoryCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.web.category.dto.LegacyWebCategoryQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.category.dto.LegacyWebTreeCategoryQueryDto;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * LegacyWebCategoryCompositeQueryDslRepository - 레거시 Web 카테고리 Composite 조회 Repository.
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
public class LegacyWebCategoryCompositeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebCategoryCompositeConditionBuilder conditionBuilder;

    public LegacyWebCategoryCompositeQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebCategoryCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    // ===== 페이징 조회 (Admin: fetchCategories) =====

    /**
     * 카테고리 페이징 조회 (일반 Offset 방식).
     *
     * @param condition 검색 조건
     * @param pageable 페이징 정보
     * @return 카테고리 목록
     */
    public List<LegacyWebCategoryQueryDto> fetchCategories(
            LegacyCategorySearchCondition condition, Pageable pageable) {
        return queryFactory
                .select(createProductCategoryProjection())
                .from(legacyCategoryEntity)
                .where(
                        conditionBuilder.displayNameLike(condition.categoryName()),
                        conditionBuilder.categoryIdEq(condition.categoryId()),
                        conditionBuilder.categoryDepthEq(condition.categoryDepth()),
                        conditionBuilder.excludeNonDisplayCategory())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(legacyCategoryEntity.id.asc())
                .fetch();
    }

    /**
     * 카테고리 페이징 조회 (No-Offset 방식).
     *
     * @param condition 검색 조건
     * @param pageable 페이징 정보
     * @return 카테고리 목록
     */
    public List<LegacyWebCategoryQueryDto> fetchCategoriesWithNoOffset(
            LegacyCategorySearchCondition condition, Pageable pageable) {
        return queryFactory
                .select(createProductCategoryProjection())
                .from(legacyCategoryEntity)
                .where(
                        conditionBuilder.displayNameLike(condition.categoryName()),
                        conditionBuilder.categoryIdEq(condition.categoryId()),
                        conditionBuilder.categoryDepthEq(condition.categoryDepth()),
                        conditionBuilder.categoryIdCondition(condition.lastCategoryId(), pageable),
                        conditionBuilder.excludeNonDisplayCategory())
                .orderBy(conditionBuilder.getOrderSpecifier(pageable))
                .limit(pageable.getPageSize())
                .fetch();
    }

    /**
     * 카테고리 개수 조회.
     *
     * @param condition 검색 조건
     * @return 카테고리 개수
     */
    public long countCategories(LegacyCategorySearchCondition condition) {
        Long count =
                queryFactory
                        .select(legacyCategoryEntity.count())
                        .from(legacyCategoryEntity)
                        .where(
                                conditionBuilder.displayNameLike(condition.categoryName()),
                                conditionBuilder.excludeNonDisplayCategory())
                        .fetchOne();
        return count != null ? count : 0L;
    }

    // ===== 전체 조회 (Admin: fetchAllCategoriesAsTree, Web: getCategories) =====

    /**
     * 전체 카테고리 조회 (트리 구성용).
     *
     * <p>categoryIds가 비어있으면 전체 조회.
     *
     * @param categoryIds 조회할 카테고리 ID 목록 (비어있으면 전체)
     * @return 카테고리 목록
     */
    public List<LegacyWebTreeCategoryQueryDto> fetchAllCategories(Set<Long> categoryIds) {
        return queryFactory
                .select(createTreeCategoryProjection())
                .from(legacyCategoryEntity)
                .where(
                        conditionBuilder.excludeNonDisplayCategory(),
                        conditionBuilder.categoryIdIn(categoryIds))
                .fetch();
    }

    /**
     * 전체 카테고리 조회 (트리 구성용, 파라미터 없음).
     *
     * @return 전체 카테고리 목록
     */
    public List<LegacyWebTreeCategoryQueryDto> fetchAllCategories() {
        return queryFactory
                .select(createTreeCategoryProjection())
                .from(legacyCategoryEntity)
                .where(conditionBuilder.excludeNonDisplayCategory())
                .fetch();
    }

    // ===== ID 기반 조회 (Admin: fetchAllParentCategoriesBulk) =====

    /**
     * 카테고리 ID 목록으로 조회.
     *
     * @param categoryIds 카테고리 ID 목록
     * @return 카테고리 목록
     */
    public List<LegacyWebTreeCategoryQueryDto> fetchCategoriesByIds(Set<Long> categoryIds) {
        return queryFactory
                .select(createTreeCategoryProjection())
                .from(legacyCategoryEntity)
                .where(
                        conditionBuilder.excludeNonDisplayCategory(),
                        conditionBuilder.categoryIdIn(categoryIds))
                .fetch();
    }

    /**
     * 카테고리 ID 목록으로 조회 (List 버전).
     *
     * @param categoryIds 카테고리 ID 목록
     * @return 카테고리 목록
     */
    public List<LegacyWebTreeCategoryQueryDto> fetchCategoriesByIds(List<Long> categoryIds) {
        return queryFactory
                .select(createTreeCategoryProjection())
                .from(legacyCategoryEntity)
                .where(
                        conditionBuilder.excludeNonDisplayCategory(),
                        conditionBuilder.categoryIdIn(categoryIds))
                .fetch();
    }

    // ===== 단건 조회 =====

    /**
     * 카테고리 단건 조회 (ID).
     *
     * @param categoryId 카테고리 ID
     * @return 카테고리 Optional
     */
    public Optional<LegacyWebCategoryQueryDto> fetchCategoryById(Long categoryId) {
        LegacyWebCategoryQueryDto dto =
                queryFactory
                        .select(createProductCategoryProjection())
                        .from(legacyCategoryEntity)
                        .where(conditionBuilder.categoryIdEq(categoryId))
                        .fetchOne();
        return Optional.ofNullable(dto);
    }

    // ===== 자식 카테고리 조회 (Admin: fetchAllChildCategories) =====

    /**
     * 직계 자식 카테고리 조회.
     *
     * <p>parentCategoryId가 일치하는 카테고리만 조회 (한 단계 아래).
     *
     * @param parentCategoryId 부모 카테고리 ID
     * @return 자식 카테고리 목록
     */
    public List<LegacyWebTreeCategoryQueryDto> fetchDirectChildCategories(Long parentCategoryId) {
        return queryFactory
                .select(createTreeCategoryProjection())
                .from(legacyCategoryEntity)
                .where(
                        conditionBuilder.parentCategoryIdEq(parentCategoryId),
                        conditionBuilder.excludeNonDisplayCategory())
                .orderBy(conditionBuilder.orderByDepthAsc())
                .fetch();
    }

    // ===== Recursive CTE용 Native Query (별도 클래스로 분리 권장) =====

    /**
     * 하위 카테고리 전체 조회 (Recursive).
     *
     * <p>Note: QueryDSL로 Recursive CTE 구현이 복잡하므로, Native Query 사용 권장.
     *
     * <p>현재는 한 단계 아래 자식만 조회하는 비재귀 방식 제공. 전체 하위 트리가 필요한 경우 Application Layer에서 반복 호출하거나 Native Query
     * Repository를 별도로 구현하세요.
     *
     * @param categoryId 기준 카테고리 ID
     * @return 자식 카테고리 목록 (한 단계 아래만)
     */
    public List<LegacyWebTreeCategoryQueryDto> fetchChildCategories(Long categoryId) {
        return fetchDirectChildCategories(categoryId);
    }

    // ===== Projection 생성 =====

    /**
     * ProductCategoryContext용 Projection 생성.
     *
     * <p>Projections.constructor() 사용 (@QueryProjection 금지).
     */
    private com.querydsl.core.types.ConstructorExpression<LegacyWebCategoryQueryDto>
            createProductCategoryProjection() {
        return Projections.constructor(
                LegacyWebCategoryQueryDto.class,
                legacyCategoryEntity.id,
                legacyCategoryEntity.categoryName,
                legacyCategoryEntity.displayName,
                legacyCategoryEntity.categoryDepth,
                legacyCategoryEntity.parentCategoryId,
                legacyCategoryEntity.path,
                legacyCategoryEntity.targetGroup.stringValue());
    }

    /**
     * TreeCategoryContext용 Projection 생성.
     *
     * <p>Projections.constructor() 사용 (@QueryProjection 금지).
     */
    private com.querydsl.core.types.ConstructorExpression<LegacyWebTreeCategoryQueryDto>
            createTreeCategoryProjection() {
        return Projections.constructor(
                LegacyWebTreeCategoryQueryDto.class,
                legacyCategoryEntity.id,
                legacyCategoryEntity.categoryName,
                legacyCategoryEntity.displayName,
                legacyCategoryEntity.categoryDepth,
                legacyCategoryEntity.parentCategoryId);
    }
}
