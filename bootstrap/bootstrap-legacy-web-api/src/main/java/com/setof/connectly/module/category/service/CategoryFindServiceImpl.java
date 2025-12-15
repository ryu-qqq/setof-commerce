package com.setof.connectly.module.category.service;

import com.setof.connectly.module.category.dto.CategoryDisplayDto;
import com.setof.connectly.module.category.repository.CategoryFindRepository;
import com.setof.connectly.module.product.dto.cateogry.CategoryDto;
import com.setof.connectly.module.product.dto.cateogry.ProductCategoryDto;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryFindServiceImpl implements CategoryFindService {

    private final CategoryFindRepository categoryFindRepository;

    private final CategoryRedisQueryService categoryRedisQueryService;

    private final CategoryRedisFindService categoryRedisFindService;

    public List<CategoryDisplayDto> getAllCategoriesAsTree() {
        List<CategoryDisplayDto> categoryDisplays =
                categoryRedisFindService.fetchAllCategoriesAsTree();
        if (!categoryDisplays.isEmpty()) return categoryDisplays;
        else {
            List<CategoryDisplayDto> categories = categoryFindRepository.getAllCategory();
            List<CategoryDisplayDto> categoryDisplaysInDb = constructTree(categories);
            categoryRedisQueryService.saveByIssueTypeAndTargetId(categoryDisplaysInDb);
            return categoryDisplaysInDb;
        }
    }

    @Override
    public List<Long> fetchCategoryIds(List<String> categoryNames) {
        List<Long> categoryIds = categoryFindRepository.fetchCategoryIds(categoryNames);

        return categoryIds.stream()
                .map(categoryFindRepository::fetchChildrenCategories)
                .flatMap(c -> c.stream().map(CategoryDisplayDto::getCategoryId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductCategoryDto> fetchCategoryList(Set<Long> categoryIds) {
        return categoryFindRepository.fetchProductCategoryList(categoryIds);
    }

    @Override
    public List<Long> fetchCategoryIdChildren(long categoryId) {
        List<?> objects =
                fetchCategory(categoryId)
                        .map(
                                category -> {
                                    switch (category.getCategoryDepth()) {
                                        case 1:
                                            return categoryFindRepository
                                                    .fetchCategoryIdsWithTarget(
                                                            category.getTargetGroup());
                                        case 2:
                                        case 3:
                                            return fetchChildrenCategory(categoryId).stream()
                                                    .map(CategoryDisplayDto::getCategoryId)
                                                    .collect(Collectors.toList());
                                        case 4:
                                            return Collections.singletonList(categoryId);
                                        default:
                                            return Collections.emptyList();
                                    }
                                })
                        .orElse(Collections.emptyList());

        return objects.stream()
                .filter(o -> (o instanceof Long))
                .map(o -> (Long) o)
                .collect(Collectors.toList());
    }

    private Optional<CategoryDto> fetchCategory(long categoryId) {
        return categoryFindRepository.fetchCategory(categoryId);
    }

    private List<CategoryDisplayDto> fetchChildrenCategory(long categoryId) {
        return categoryFindRepository.fetchChildrenCategories(categoryId);
    }

    private List<CategoryDisplayDto> constructTree(List<CategoryDisplayDto> categories) {
        Map<Long, CategoryDisplayDto> categoryMap =
                categories.stream()
                        .collect(
                                Collectors.toMap(
                                        CategoryDisplayDto::getCategoryId, Function.identity()));

        List<CategoryDisplayDto> roots = new ArrayList<>();
        for (CategoryDisplayDto category : categories) {
            CategoryDisplayDto dto = categoryMap.get(category.getCategoryId());
            if (category.getCategoryDepth() == 1) {
                roots.add(dto);
            } else {
                CategoryDisplayDto parent = categoryMap.get(category.getParentCategoryId());
                parent.getChildren().add(dto);
            }
        }
        return roots;
    }
}
