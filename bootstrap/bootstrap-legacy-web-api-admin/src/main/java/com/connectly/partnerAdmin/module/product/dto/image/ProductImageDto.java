package com.connectly.partnerAdmin.module.product.dto.image;

import com.connectly.partnerAdmin.module.product.enums.image.ProductGroupImageType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImageDto {


    private ProductGroupImageType type;
    private String productImageUrl;

    @QueryProjection
    public ProductImageDto(ProductGroupImageType type, String productImageUrl) {
        this.type = type;
        this.productImageUrl = productImageUrl;
    }

    @Override
    public int hashCode() {
        return (type + productImageUrl).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ProductImageDto p) {
            return this.hashCode()==p.hashCode();
        }
        return false;
    }


}
