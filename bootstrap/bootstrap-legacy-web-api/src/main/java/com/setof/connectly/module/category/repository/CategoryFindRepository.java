package com.setof.connectly.module.category.repository;

import com.setof.connectly.module.category.dto.CategoryDisplayDto;
import com.setof.connectly.module.category.enums.TargetGroup;
import com.setof.connectly.module.product.dto.cateogry.CategoryDto;
import com.setof.connectly.module.product.dto.cateogry.ProductCategoryDto;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryFindRepository {

    List<CategoryDisplayDto> getAllCategory();

    Optional<CategoryDto> fetchCategory(long categoryId);

    List<Long> fetchCategoryIdsWithTarget(TargetGroup targetGroup);

    List<Long> fetchCategoryIds(List<String> categoryNames);

    List<CategoryDisplayDto> fetchChildrenCategories(long categoryId);

    List<ProductCategoryDto> fetchProductCategoryList(Set<Long> categoryIds);
}
