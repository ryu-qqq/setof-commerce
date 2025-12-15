package com.setof.connectly.module.brand.dto;

import com.querydsl.core.annotations.QueryProjection;

public class BrandDisplayDto {

    private final long brandId;
    private final String brandName;
    private final String korBrandName;
    private final String brandIconImageUrl;

    @QueryProjection
    public BrandDisplayDto(
            long brandId, String brandName, String korBrandName, String brandIconImageUrl) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.korBrandName = korBrandName;
        this.brandIconImageUrl = brandIconImageUrl;
    }

    public long getBrandId() {
        return brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getKorBrandName() {
        return korBrandName;
    }

    public String getBrandIconImageUrl() {
        return brandIconImageUrl;
    }
}
