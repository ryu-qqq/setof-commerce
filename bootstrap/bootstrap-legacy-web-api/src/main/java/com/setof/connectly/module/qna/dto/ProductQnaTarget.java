package com.setof.connectly.module.qna.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.product.dto.brand.BrandDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductQnaTarget implements QnaTarget {

    private long productGroupId;
    private String productGroupName;
    private String productGroupMainImageUrl;
    private BrandDto brand;

    @QueryProjection
    public ProductQnaTarget(
            long productGroupId,
            String productGroupName,
            String productGroupMainImageUrl,
            BrandDto brand) {
        this.productGroupId = productGroupId;
        this.productGroupName = productGroupName;
        this.productGroupMainImageUrl = productGroupMainImageUrl;
        this.brand = brand;
    }
}
