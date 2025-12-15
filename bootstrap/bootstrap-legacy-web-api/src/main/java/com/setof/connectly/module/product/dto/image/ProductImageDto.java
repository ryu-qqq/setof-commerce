package com.setof.connectly.module.product.dto.image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.product.enums.image.ProductGroupImageType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImageDto {

    @JsonIgnore private long productGroupImageId;
    private ProductGroupImageType productGroupImageType;
    private String imageUrl;

    @QueryProjection
    public ProductImageDto(
            long productGroupImageId,
            ProductGroupImageType productGroupImageType,
            String imageUrl) {
        this.productGroupImageId = productGroupImageId;
        this.productGroupImageType = productGroupImageType;
        this.imageUrl = imageUrl;
    }

    @Override
    public int hashCode() {
        return (productGroupImageId + imageUrl).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProductImageDto) {
            ProductImageDto p = (ProductImageDto) obj;
            return this.hashCode() == p.hashCode();
        }
        return false;
    }
}
