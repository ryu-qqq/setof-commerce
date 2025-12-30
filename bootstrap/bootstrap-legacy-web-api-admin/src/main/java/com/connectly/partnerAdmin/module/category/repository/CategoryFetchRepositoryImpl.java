package com.connectly.partnerAdmin.module.category.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.connectly.partnerAdmin.module.category.core.ExternalCategoryContext;
import com.connectly.partnerAdmin.module.category.core.ProductCategoryContext;
import com.connectly.partnerAdmin.module.category.core.QExternalCategoryContext;
import com.connectly.partnerAdmin.module.category.core.QProductCategoryContext;
import com.connectly.partnerAdmin.module.category.core.QTreeCategoryContext;
import com.connectly.partnerAdmin.module.category.core.TreeCategoryContext;
import com.connectly.partnerAdmin.module.category.entity.Category;
import com.connectly.partnerAdmin.module.category.entity.QCategory;
import com.connectly.partnerAdmin.module.category.filter.CategoryFilter;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.sql.JPASQLQuery;
import com.querydsl.sql.SQLExpressions;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.Union;

import static com.connectly.partnerAdmin.module.category.entity.QCategory.category;
import static com.connectly.partnerAdmin.module.category.entity.QCategoryMapping.categoryMapping;

@Repository
@RequiredArgsConstructor
public class CategoryFetchRepositoryImpl implements CategoryFetchRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public boolean hasCategoryIdExist(long categoryId) {
        Integer integer = queryFactory
                .selectOne()
                .from(category)
                .where(categoryIdEq(categoryId), exclusiveNonDisplayCategory())
                .fetchFirst();

        return integer != null;
    }

    @Override
    public Optional<TreeCategoryContext> fetchById(long categoryId) {
        return Optional.ofNullable(queryFactory
            .select(
                new QTreeCategoryContext(category.id, category.categoryName, category.displayName, category.categoryDepth, category.parentCategoryId)
            )
            .from(category)
            .where(categoryIdEq(categoryId), exclusiveNonDisplayCategory())
            .fetchFirst());
    }


    @Override
    public Set<Long> fetchCategoryChildrenIds(long categoryId) {
        JPASQLQuery<Category> q = new JPASQLQuery<>(em, SQLTemplates.DEFAULT);
        QCategory c = new QCategory("c");
        QCategory sub = new QCategory("sub");
        EntityPathBase<QCategory> rec = new EntityPathBase<>(QCategory.class, "sub");

        JPQLQuery<Category> query1 = JPAExpressions.select(Projections.fields(Category.class, c.id, c.parentCategoryId, c.categoryDepth)).from(c)
                .where(c.id.eq(categoryId));

        JPQLQuery<Category> query2 =
                JPAExpressions.select(Projections.fields(Category.class, c.id, c.parentCategoryId, c.categoryDepth)).from(rec)
                        .innerJoin(c).on(sub.id.eq(c.parentCategoryId));

        Union<Category> union = SQLExpressions.unionAll(query1, query2);

        List<Long> categoryIds = q.withRecursive(rec, c.id, c.parentCategoryId, c.categoryDepth).as(union)
                .select(sub.id)
                .from(rec)
                .fetch();

        return new HashSet<>(categoryIds);
    }



    @Override
    public List<TreeCategoryContext> fetchAllChildCategories(long categoryId){
        JPASQLQuery<Category> q = new JPASQLQuery<>(em, SQLTemplates.DEFAULT);
        QCategory c = new QCategory("c");
        QCategory sub = new QCategory("sub");
        EntityPathBase<QCategory> rec = new EntityPathBase<>(QCategory.class, "sub");

        JPQLQuery<Category> query1 = JPAExpressions.select(Projections.fields(Category.class, c.id, c.categoryName, c.displayName, c.parentCategoryId, c.categoryDepth)).from(c)
                .where(c.id.eq(categoryId));

        JPQLQuery<Category> query2 =
                JPAExpressions.select(Projections.fields(Category.class,  c.id, c.categoryName, c.displayName, c.parentCategoryId, c.categoryDepth)).from(rec)
                        .innerJoin(c).on(sub.id.eq(c.parentCategoryId));

        Union<Category> union = SQLExpressions.unionAll(query1, query2);

        return q.withRecursive(rec, c.id, c.categoryName, c.displayName, c.parentCategoryId, c.categoryDepth).as(union)
                .select(
                        new QTreeCategoryContext(sub.id, sub.categoryName, sub.displayName, sub.categoryDepth, sub.parentCategoryId)
                )
                .from(rec).orderBy(sub.categoryDepth.asc())
                .fetch();
    }

    @Override
    public List<TreeCategoryContext> fetchAllParentCategories(long categoryId){
        JPASQLQuery<Category> q = new JPASQLQuery<>(em, SQLTemplates.DEFAULT);
        QCategory c = new QCategory("c");
        QCategory sub = new QCategory("sub");
        EntityPathBase<QCategory> rec = new EntityPathBase<>(QCategory.class, "sub");

        JPQLQuery<Category> query1 = JPAExpressions.select(Projections.fields(Category.class, c.id, c.categoryName, c.displayName, c.parentCategoryId, c.categoryDepth)).from(c)
                .where(c.id.eq(categoryId));

        JPQLQuery<Category> query2 =
                JPAExpressions.select(Projections.fields(Category.class,  c.id, c.categoryName, c.displayName, c.parentCategoryId, c.categoryDepth)).from(rec)
                        .innerJoin(c).on(c.id.eq(sub.parentCategoryId));

        Union<Category> union = SQLExpressions.unionAll(query1, query2);

        return q.withRecursive(rec, c.id, c.categoryName, c.displayName, c.parentCategoryId, c.categoryDepth).as(union)
                .select(
                        new QTreeCategoryContext(sub.id, sub.categoryName, sub.displayName, sub.categoryDepth, sub.parentCategoryId)
                )
                .from(rec).orderBy(sub.categoryDepth.desc())
                .fetch();
    }


    @Override
    public List<TreeCategoryContext> fetchAllCategories(Set<Long> categoryIds) {
        return queryFactory
                .select(

                        new QTreeCategoryContext(
                                category.id,
                                category.categoryName,
                                category.displayName,
                                category.categoryDepth,
                                category.parentCategoryId
                        )
                )
                .from(category)
                .where(exclusiveNonDisplayCategory(), categoryIdIn(categoryIds))
                .fetch();
    }

    @Override
    public List<ProductCategoryContext> fetchProductCategoryContexts(CategoryFilter filter, Pageable pageable) {
        return queryFactory
                .select(
                        new QProductCategoryContext(
                                category.id,
                                category.categoryName,
                                category.displayName,
                                category.categoryDepth,
                                category.path,
                                category.targetGroup
                        )
                )
                .from(category)
                .where(isCategoryNameLike(filter), categoryIdEq(filter.getCategoryId()), categoryDepthEq(filter))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(category.id.asc())
                .fetch();
    }


    @Override
    public List<ProductCategoryContext> fetchProductCategoryContextsWithNoOffset(CategoryFilter filter, Pageable pageable) {
        return queryFactory
                .select(
                        new QProductCategoryContext(
                                category.id,
                                category.categoryName,
                                category.displayName,
                                category.categoryDepth,
                                category.path,
                                category.targetGroup
                        )
                )
                .from(category)
                .where( isCategoryNameLike(filter),
                        categoryIdEq(filter.getCategoryId()),
                        categoryDepthEq(filter),
                        getCategoryIdCondition(filter, pageable)
                )
                .orderBy(getOrderSpecifier(pageable))
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public JPAQuery<Long> fetchCategoryCountQuery(CategoryFilter filter){
        return queryFactory
                .select(category.count())
                .from(category)
                .where(isCategoryNameLike(filter), exclusiveNonDisplayCategory())
                .distinct();
    }

    @Override
    public List<ExternalCategoryContext> fetchExternalCategories(long siteId, List<String> mappingCategoryIds) {
        return queryFactory
                .select(
                        new QExternalCategoryContext(
                                category.id,
                                category.categoryName,
                                category.displayName,
                                category.categoryDepth,
                                categoryMapping.siteId,
                                categoryMapping.mappingCategoryId,
                                categoryMapping.description
                        )
                )
                .from(category)
                .innerJoin(categoryMapping).on(categoryMapping.categoryId.eq(category.id))
                .where(mappingCategoryIdIn(mappingCategoryIds), siteIdEq(siteId))
                .where()
                .fetch();
    }

    @Override
    public List<ExternalCategoryContext> fetchExternalCategoriesByOurCategoryIds(long siteId, Set<Long> categoryIds) {
        return queryFactory
                .select(
                        new QExternalCategoryContext(
                                category.id,
                                category.categoryName,
                                category.displayName,
                                category.categoryDepth,
                                categoryMapping.siteId,
                                categoryMapping.mappingCategoryId,
                                categoryMapping.description.coalesce("")
                        )
                )
                .from(category)
                .innerJoin(categoryMapping).on(categoryMapping.categoryId.eq(category.id))
                .where(categoryIdIn(categoryIds), siteIdEq(siteId))
                .where()
                .fetch();
    }

    public Long fetchBySiteIdAndMappingCategoryId(long siteId, String mappingCategoryId) {
        return queryFactory
            .select(
                category.id
            )
            .from(category)
            .innerJoin(categoryMapping).on(categoryMapping.categoryId.eq(category.id))
            .where(siteIdEq(siteId), categoryMapping.mappingCategoryId.eq(mappingCategoryId))
            .fetchOne();
    }


    private BooleanExpression categoryIdEq(Long categoryId){
        if(categoryId !=null) return category.id.eq(categoryId);
        else return null;
    }

    private BooleanExpression exclusiveNonDisplayCategory(){
        return category.id.notIn(1828L);
    }


    private BooleanExpression categoryIdIn(Set<Long> categoryIds){
        if(categoryIds.isEmpty()) return null;
        else return category.id.in(categoryIds);
    }

    private BooleanExpression getCategoryIdCondition(CategoryFilter filter, Pageable pageable) {
        Sort.Order order = pageable.getSort().getOrderFor("id");
        if (order != null && order.isAscending()) {
            return isCategoryIdGt(filter);
        } else {
            return isCategoryIdLt(filter);
        }
    }

    private BooleanExpression isCategoryIdLt(CategoryFilter filter){
        if(filter.getLastDomainId() !=null) return category.id.lt(filter.getLastDomainId());
        else return null;
    }

    private BooleanExpression isCategoryIdGt(CategoryFilter filter){
        if(filter.getLastDomainId() !=null) return category.id.gt(filter.getLastDomainId());
        else return null;
    }


    private OrderSpecifier<?> getOrderSpecifier(Pageable pageable) {
        Sort.Order order = pageable.getSort().getOrderFor("id");
        if (order != null && order.isAscending()) {
            return category.id.asc();
        } else {
            return category.id.desc();
        }
    }


    private BooleanExpression isCategoryNameLike(CategoryFilter filter){
        if(StringUtils.hasText(filter.getCategoryName())){
            return category.displayName.contains(filter.getCategoryName());
        }
        else return null;
    }

    private BooleanExpression categoryDepthEq(CategoryFilter filter){
        if(filter.getCategoryDepth() !=null) return category.categoryDepth.eq(filter.getCategoryDepth());
        else return null;
    }


    private BooleanExpression mappingCategoryIdIn(List<String> mappingCategoryIds){
        return categoryMapping.mappingCategoryId.in(mappingCategoryIds);
    }

    private BooleanExpression siteIdEq(long siteId){
        return categoryMapping.siteId.eq(siteId);
    }


}
