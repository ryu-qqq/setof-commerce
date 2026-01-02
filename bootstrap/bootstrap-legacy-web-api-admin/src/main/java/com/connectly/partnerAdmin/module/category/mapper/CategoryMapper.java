package com.connectly.partnerAdmin.module.category.mapper;

import com.connectly.partnerAdmin.module.category.core.TreeCategoryContext;

import java.util.List;

public interface CategoryMapper {

    List<TreeCategoryContext> constructTree(List<TreeCategoryContext> categories);

}
