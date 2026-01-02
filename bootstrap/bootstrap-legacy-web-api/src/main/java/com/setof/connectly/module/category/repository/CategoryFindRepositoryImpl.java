package com.setof.connectly.module.category.repository;

import static com.querydsl.core.types.Projections.constructor;
import static com.setof.connectly.module.category.entity.QCategory.category;
import static com.setof.connectly.module.product.entity.group.QProductGroup.productGroup;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.category.dto.CategoryDisplayDto;
import com.setof.connectly.module.category.enums.TargetGroup;
import com.setof.connectly.module.product.dto.cateogry.CategoryDto;
import com.setof.connectly.module.product.dto.cateogry.ProductCategoryDto;
import com.setof.connectly.module.product.dto.cateogry.QProductCategoryDto;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryFindRepositoryImpl implements CategoryFindRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public List<CategoryDisplayDto> getAllCategory() {
        return queryFactory
                .select(
                        constructor(
                                CategoryDisplayDto.class,
                                category.id,
                                category.displayName,
                                category.parentCategoryId,
                                category.categoryDepth))
                .from(category)
                .where(category.id.notIn(1828L))
                .fetch();
    }

    @Override
    public Optional<CategoryDto> fetchCategory(long categoryId) {
        CategoryDto categoryDto =
                queryFactory
                        .select(
                                constructor(
                                        CategoryDto.class,
                                        category.id,
                                        category.displayName,
                                        category.categoryDepth,
                                        category.targetGroup,
                                        category.path))
                        .from(category)
                        .where(category.id.eq(categoryId))
                        .fetchOne();
        return Optional.ofNullable(categoryDto);
    }

    @Override
    public List<Long> fetchCategoryIdsWithTarget(TargetGroup targetGroup) {
        return queryFactory
                .select(category.id)
                .from(category)
                .where(category.targetGroup.eq(targetGroup))
                .distinct()
                .fetch();
    }

    @Override
    public List<Long> fetchCategoryIds(List<String> categoryNames) {
        return queryFactory
                .select(category.id)
                .from(category)
                .where(categoryNameIn(categoryNames))
                .distinct()
                .fetch();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CategoryDisplayDto> fetchChildrenCategories(long categoryId) {
        String sql = """
            WITH RECURSIVE sub (category_id, category_name, parent_category_id, category_depth) AS (
                SELECT c.category_id, c.category_name, c.parent_category_id, c.category_depth
                FROM category c
                WHERE c.category_id = :categoryId
                UNION ALL
                SELECT c.category_id, c.category_name, c.parent_category_id, c.category_depth
                FROM sub
                INNER JOIN category c ON sub.category_id = c.parent_category_id
            )
            SELECT sub.category_id, sub.category_name, sub.parent_category_id, sub.category_depth
            FROM sub
            ORDER BY sub.category_depth DESC
            """;

        List<Object[]> results = em.createNativeQuery(sql)
                .setParameter("categoryId", categoryId)
                .getResultList();

        return results.stream()
                .map(row -> new CategoryDisplayDto(
                        ((Number) row[0]).longValue(),
                        (String) row[1],
                        ((Number) row[2]).longValue(),
                        ((Number) row[3]).intValue()))
                .toList();
    }

    @Override
    public List<ProductCategoryDto> fetchProductCategoryList(Set<Long> categoryIds) {
        return queryFactory
                .from(category)
                .leftJoin(productGroup)
                .on(category.id.eq(productGroup.productGroupDetails.categoryId))
                .where(categoryIdIn(categoryIds))
                .orderBy(category.id.asc())
                .transform(
                        GroupBy.groupBy(category.id)
                                .list(
                                        new QProductCategoryDto(
                                                productGroup.id,
                                                category.id,
                                                category.categoryName,
                                                category.displayName,
                                                category.parentCategoryId,
                                                category.categoryDepth)));
    }

    private BooleanExpression categoryNameIn(List<String> categoryNames) {
        return category.categoryName.in(categoryNames);
    }

    private BooleanExpression categoryIdIn(Set<Long> categoryIds) {
        return category.id.in(categoryIds);
    }
}
