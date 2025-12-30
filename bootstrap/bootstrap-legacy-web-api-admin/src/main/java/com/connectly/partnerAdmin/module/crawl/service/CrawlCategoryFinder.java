package com.connectly.partnerAdmin.module.crawl.service;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.category.repository.CategoryFetchRepository;

@Component
public class CrawlCategoryFinder {

    private final CategoryFetchRepository categoryFetchRepository;

    public CrawlCategoryFinder(CategoryFetchRepository categoryFetchRepository) {
        this.categoryFetchRepository = categoryFetchRepository;
    }

    public long fetchByCategoryName(String mappingCategoryId) {
        Long l = categoryFetchRepository.fetchBySiteIdAndMappingCategoryId(5, mappingCategoryId);
        if(l == null) {
            throw new RuntimeException("외부 카테고리가 존재하지 않습니다. " + mappingCategoryId);
        }
        return l;
    }


}
