package com.connectly.partnerAdmin.module.product.mapper.group;

import com.connectly.partnerAdmin.module.category.core.TreeCategoryContext;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupDetailResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProductGroupCategoryMapperImpl implements ProductGroupCategoryMapper{

    @Override
    public void setCategoryFullPath(List<ProductGroupDetailResponse> productGroupDetailResponses, List<TreeCategoryContext> treeCategoryContexts) {
        Map<Long, TreeCategoryContext> categoryMap = createCategoryMap(treeCategoryContexts);

        for(ProductGroupDetailResponse pg: productGroupDetailResponses){
            String categoryFullName = buildCategoryPath(pg, categoryMap);
            pg.getProductGroup().setCategoryFullName(categoryFullName);
        }
    }


    private Map<Long, TreeCategoryContext> createCategoryMap(List<TreeCategoryContext> categories) {
        return categories.stream()
                .collect(Collectors.toMap(TreeCategoryContext::getCategoryId, category -> category));
    }

    private String buildCategoryPath(ProductGroupDetailResponse productGroup, Map<Long, TreeCategoryContext> categoryMap) {
        String[] categoryIds = productGroup.getProductGroup().getPath().split(",");

        return Arrays.stream(categoryIds)
                .mapToLong(Long::parseLong)
                .mapToObj(categoryMap::get)
                .map(TreeCategoryContext::getDisplayName)
                .collect(Collectors.joining(">"));
    }


}
