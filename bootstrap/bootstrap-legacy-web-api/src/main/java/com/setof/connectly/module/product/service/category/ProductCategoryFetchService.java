package com.setof.connectly.module.product.service.category;

import com.setof.connectly.module.product.dto.cateogry.ProductCategoryDto;
import java.util.List;
import java.util.Set;

public interface ProductCategoryFetchService {

    List<ProductCategoryDto> fetchProductCategories(String categoryIds);

    List<ProductCategoryDto> fetchProductCategories(Set<String> categoryIds);

    List<Long> fetchCategoryChildren(long categoryId);
}
