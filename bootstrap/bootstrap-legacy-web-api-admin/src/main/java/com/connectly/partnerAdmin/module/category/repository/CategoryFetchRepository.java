package com.connectly.partnerAdmin.module.category.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Pageable;

import com.connectly.partnerAdmin.module.category.core.ExternalCategoryContext;
import com.connectly.partnerAdmin.module.category.core.ProductCategoryContext;
import com.connectly.partnerAdmin.module.category.core.TreeCategoryContext;
import com.connectly.partnerAdmin.module.category.filter.CategoryFilter;
import com.querydsl.jpa.impl.JPAQuery;

public interface CategoryFetchRepository {

    boolean hasCategoryIdExist(long categoryId);
    Set<Long> fetchCategoryChildrenIds(long categoryId);
    Optional<TreeCategoryContext> fetchById(long categoryId);
    List<TreeCategoryContext> fetchAllChildCategories(long categoryId);
    List<TreeCategoryContext> fetchAllParentCategories(long categoryId);
    List<TreeCategoryContext> fetchAllCategories(Set<Long> categoryIds);


    List<ProductCategoryContext> fetchProductCategoryContexts(CategoryFilter filter, Pageable pageable);
    List<ProductCategoryContext> fetchProductCategoryContextsWithNoOffset(CategoryFilter filter, Pageable pageable);
    JPAQuery<Long> fetchCategoryCountQuery(CategoryFilter filter);

    List<ExternalCategoryContext> fetchExternalCategories(long siteId, List<String> mappingCategoryIds);

    List<ExternalCategoryContext> fetchExternalCategoriesByOurCategoryIds(long siteId, Set<Long> categoryIds);
    Long fetchBySiteIdAndMappingCategoryId(long siteId, String mappingCategoryId);
}
