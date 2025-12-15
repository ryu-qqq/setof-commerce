package com.setof.connectly.module.category.service;

import com.setof.connectly.module.category.dto.CategoryDisplayDto;
import com.setof.connectly.module.product.dto.cateogry.ProductCategoryDto;
import java.util.List;
import java.util.Set;

public interface CategoryFindService {

    List<CategoryDisplayDto> getAllCategoriesAsTree();

    List<Long> fetchCategoryIds(List<String> categoryNames);

    List<ProductCategoryDto> fetchCategoryList(Set<Long> categoryIds);

    List<Long> fetchCategoryIdChildren(long categoryId);
}
