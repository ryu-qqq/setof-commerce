package com.setof.connectly.module.category.repository;

import static com.querydsl.core.types.Projections.constructor;
import static com.setof.connectly.module.category.entity.QCategory.category;
import static com.setof.connectly.module.product.entity.group.QProductGroup.productGroup;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.sql.JPASQLQuery;
import com.querydsl.sql.SQLExpressions;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.Union;
import com.setof.connectly.module.category.dto.CategoryDisplayDto;
import com.setof.connectly.module.category.dto.QCategoryDisplayDto;
import com.setof.connectly.module.category.entity.Category;
import com.setof.connectly.module.category.entity.QCategory;
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
    public List<CategoryDisplayDto> fetchChildrenCategories(long categoryId) {
        JPASQLQuery<Category> q = new JPASQLQuery<>(em, SQLTemplates.DEFAULT);
        QCategory c = new QCategory("c");
        QCategory sub = new QCategory("sub");
        EntityPathBase<QCategory> rec = new EntityPathBase<>(QCategory.class, "sub");

        JPQLQuery<Category> query1 =
                JPAExpressions.select(
                                Projections.fields(
                                        Category.class,
                                        c.id,
                                        c.categoryName,
                                        c.parentCategoryId,
                                        c.categoryDepth))
                        .from(c)
                        .where(c.id.eq(categoryId));

        JPQLQuery<Category> query2 =
                JPAExpressions.select(
                                Projections.fields(
                                        Category.class,
                                        c.id,
                                        c.categoryName,
                                        c.parentCategoryId,
                                        c.categoryDepth))
                        .from(rec)
                        .innerJoin(c)
                        .on(sub.id.eq(c.parentCategoryId));

        Union<Category> union = SQLExpressions.unionAll(query1, query2);

        return q.withRecursive(rec, c.id, c.categoryName, c.parentCategoryId, c.categoryDepth)
                .as(union)
                .select(
                        new QCategoryDisplayDto(
                                sub.id, sub.categoryName, sub.parentCategoryId, sub.categoryDepth))
                .from(rec)
                .orderBy(sub.categoryDepth.desc())
                .fetch();
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
