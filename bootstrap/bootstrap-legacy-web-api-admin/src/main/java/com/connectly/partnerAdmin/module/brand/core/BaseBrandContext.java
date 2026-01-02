package com.connectly.partnerAdmin.module.brand.core;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseBrandContext implements BrandContext {

    private long brandId;
    private String brandName;

    @QueryProjection
    public BaseBrandContext(long brandId, String brandName) {
        this.brandId = brandId;
        this.brandName = brandName;
    }
}