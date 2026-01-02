package com.connectly.partnerAdmin.module.crawl.service;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.crawl.dto.request.CrawlProductGroupInsertRequestDto;
import com.connectly.partnerAdmin.module.crawl.entity.CrawlProduct;
import com.connectly.partnerAdmin.module.external.enums.MappingStatus;

@Component
public class CrawlProductConverter {

    private final CrawlCategoryFinder categoryFinder;
    private final CrawlBrandFinder brandFinder;

    public CrawlProductConverter(CrawlCategoryFinder categoryFinder, CrawlBrandFinder brandFinder) {
        this.categoryFinder = categoryFinder;
        this.brandFinder = brandFinder;
    }


    public CrawlProduct convert(CrawlProductGroupInsertRequestDto requestDto) {

        long categoryId = categoryFinder.fetchByCategoryName(getCategoryName(requestDto));
        long brandId = brandFinder.fetchByMappingBrandId(requestDto.getBrandCode());

        return new CrawlProduct(
            null,
            null,
            requestDto.getItemNo(),
            5L,
            99L,
            brandId,
            categoryId,
            "",
            MappingStatus.ACTIVE
        );
    }



    private String getCategoryName(CrawlProductGroupInsertRequestDto requestDto) {
        String headerCategory = Optional.ofNullable(requestDto.getHeaderCategoryCode()).orElse("");
        headerCategory = headerCategory.replace("[", "").replace("]", "");

        if (headerCategory.equals("W, M") || headerCategory.equals("W,M") || headerCategory.equals("WM")) {
            headerCategory = "M";
        }

        String categoryCode = Optional.ofNullable(requestDto.getSmallCategoryCode())
            .orElse(Optional.ofNullable(requestDto.getMediumCategoryCode()).orElse(""));

        return headerCategory + categoryCode;
    }

}
