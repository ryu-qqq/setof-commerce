package com.connectly.partnerAdmin.module.category.service;


import com.connectly.partnerAdmin.module.category.core.CategoryMappingInfo;
import com.connectly.partnerAdmin.module.category.core.ExternalCategoryContext;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class CategoryMappingManagerImpl implements CategoryMappingManager {

    private final CategoryFetchService categoryFetchService;

    @Override
    public void convertExternalCategoryToInternalCategory(long siteId, List<CategoryMappingInfo> categoryMappingInfos) {
        Set<String> mappingCategoryIdSet = categoryMappingInfos.stream()
                .map(CategoryMappingInfo::getCategoryMappingId)
                .collect(Collectors.toSet());

        List<ExternalCategoryContext> externalCategoryContexts = categoryFetchService.fetchExternalCategories(siteId, new ArrayList<>(mappingCategoryIdSet));


        Map<String, ExternalCategoryContext> categoryIdMap = externalCategoryContexts.stream()
                .collect(Collectors.toMap(ExternalCategoryContext::getMappingCategoryId, Function.identity(), (v1, v2) -> v1));

        for (CategoryMappingInfo categoryMappingInfo : categoryMappingInfos) {
            ExternalCategoryContext externalCategoryContext = categoryIdMap.get(categoryMappingInfo.getCategoryMappingId());
            if (externalCategoryContext == null) {
                log.error(String.format("Site %s Category mapping id [%s] not found", SiteName.of(siteId).getName(), categoryMappingInfo.getCategoryMappingId()));
            } else {
                long categoryId = externalCategoryContext.getCategoryId();
                categoryMappingInfo.setCategoryId(categoryId);
            }
        }

    }



}
