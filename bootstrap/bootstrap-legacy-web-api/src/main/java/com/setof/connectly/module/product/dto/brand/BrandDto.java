package com.setof.connectly.module.product.dto.brand;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandDto {

    private long brandId;
    private String brandName;

    @Builder
    @QueryProjection
    public BrandDto(long brandId, String brandName) {
        this.brandId = brandId;
        this.brandName = brandName;
    }
}
