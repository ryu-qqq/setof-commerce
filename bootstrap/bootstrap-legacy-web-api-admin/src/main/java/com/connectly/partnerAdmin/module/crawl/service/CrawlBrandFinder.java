package com.connectly.partnerAdmin.module.crawl.service;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.brand.repository.BrandFetchRepository;

@Component
public class CrawlBrandFinder {

    private final BrandFetchRepository brandFetchRepository;

    public CrawlBrandFinder(BrandFetchRepository brandFetchRepository) {
        this.brandFetchRepository = brandFetchRepository;
    }

    public long fetchByMappingBrandId(String mappingBrandId) {
        Long l = brandFetchRepository.fetchBySiteIdAndMappingBrandId(5, mappingBrandId);
        if(l == null) {
            throw new RuntimeException("외부 브랜드가 존재하지 않습니다. " + mappingBrandId);
        }
        return l;
    }


}
