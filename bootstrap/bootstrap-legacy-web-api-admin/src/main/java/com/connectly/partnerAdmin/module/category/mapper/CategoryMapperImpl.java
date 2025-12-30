package com.connectly.partnerAdmin.module.category.mapper;

import com.connectly.partnerAdmin.module.category.core.TreeCategoryContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CategoryMapperImpl implements CategoryMapper{


    @Override
    public List<TreeCategoryContext> constructTree(List<TreeCategoryContext> categories) {
        Map<Long, TreeCategoryContext> categoryMap = categories.stream()
                .collect(Collectors.toMap(TreeCategoryContext::getCategoryId, Function.identity()));

        List<TreeCategoryContext> roots = new ArrayList<>();
        for (TreeCategoryContext category : categories) {
            TreeCategoryContext dto = categoryMap.get(category.getCategoryId());
            if (category.getCategoryDepth() == 1) {
                roots.add(dto);
            } else {
                TreeCategoryContext parent = categoryMap.get(category.getParentCategoryId());
                parent.getChildren().add(dto);
            }
        }

        return roots;
    }



}
