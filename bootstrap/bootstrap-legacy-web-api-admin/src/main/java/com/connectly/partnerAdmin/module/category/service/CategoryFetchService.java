package com.connectly.partnerAdmin.module.category.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Pageable;

import com.connectly.partnerAdmin.module.category.core.ExternalCategoryContext;
import com.connectly.partnerAdmin.module.category.core.ProductCategoryContext;
import com.connectly.partnerAdmin.module.category.core.TreeCategoryContext;
import com.connectly.partnerAdmin.module.category.filter.CategoryFilter;
import com.connectly.partnerAdmin.module.common.dto.CustomPageable;

public interface CategoryFetchService {

    boolean hasCategoryIdExist(long categoryId);
    Optional<TreeCategoryContext> fetchById(long categoryId);
    Set<Long> fetchCategoryChildrenIds(long categoryId);

    List<TreeCategoryContext> fetchAllChildCategories(long categoryId);
    List<TreeCategoryContext> fetchAllParentCategories(long categoryId);
    List<TreeCategoryContext> fetchAllCategories();
    List<TreeCategoryContext> fetchAllCategories(Set<Long> categoryIds);

    CustomPageable<ProductCategoryContext> fetchCategories(CategoryFilter filter, Pageable pageable);

    List<ExternalCategoryContext> fetchExternalCategories(long siteId, List<String> mappingCategoryIds);
    List<ExternalCategoryContext> fetchExternalCategoriesByOurCategoryIds(long siteId, Set<Long> categoryIds);


}
