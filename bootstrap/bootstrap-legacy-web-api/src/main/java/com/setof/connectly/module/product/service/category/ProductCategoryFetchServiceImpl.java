package com.setof.connectly.module.product.service.category;

import com.setof.connectly.module.category.service.CategoryFindServiceImpl;
import com.setof.connectly.module.product.dto.cateogry.ProductCategoryDto;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductCategoryFetchServiceImpl implements ProductCategoryFetchService {

    private final CategoryFindServiceImpl categoryFindService;

    @Override
    public List<ProductCategoryDto> fetchProductCategories(String categoryIds) {
        Set<Long> categorySet = splitCategoryIds(categoryIds);
        return getCategoryList(categorySet);
    }

    @Override
    public List<ProductCategoryDto> fetchProductCategories(Set<String> categoryIds) {
        Set<Long> categorySet = stringToLong(categoryIds);
        return getCategoryList(categorySet);
    }

    @Override
    public List<Long> fetchCategoryChildren(long categoryId) {
        return categoryFindService.fetchCategoryIdChildren(categoryId);
    }

    private List<ProductCategoryDto> getCategoryList(Set<Long> categoryIds) {
        return categoryFindService.fetchCategoryList(categoryIds);
    }

    private Set<Long> stringToLong(Set<String> categoryIds) {
        return categoryIds.stream()
                .map(this::splitCategoryIds)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Set<Long> splitCategoryIds(String path) {
        return Arrays.stream(path.split(",")).map(Long::parseLong).collect(Collectors.toSet());
    }
}
