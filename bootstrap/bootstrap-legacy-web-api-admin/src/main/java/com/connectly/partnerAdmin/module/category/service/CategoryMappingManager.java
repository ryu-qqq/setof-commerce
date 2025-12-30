package com.connectly.partnerAdmin.module.category.service;

import com.connectly.partnerAdmin.module.category.core.CategoryMappingInfo;

import java.util.List;

public interface CategoryMappingManager {

    void convertExternalCategoryToInternalCategory(long siteId, List<CategoryMappingInfo> categoryMappingInfos);

}
