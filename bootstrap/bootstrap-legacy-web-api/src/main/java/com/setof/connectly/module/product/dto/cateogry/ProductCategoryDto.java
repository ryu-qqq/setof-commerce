package com.setof.connectly.module.product.dto.cateogry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCategoryDto {
    @JsonIgnore private long productGroupId;
    private long categoryId;
    private String categoryName;
    private String displayName;
    private long parentCategoryId;
    private int categoryDepth;

    @QueryProjection
    public ProductCategoryDto(
            long productGroupId,
            long categoryId,
            String categoryName,
            String displayName,
            long parentCategoryId,
            int categoryDepth) {
        this.productGroupId = productGroupId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.displayName = displayName;
        this.parentCategoryId = parentCategoryId;
        this.categoryDepth = categoryDepth;
    }
}
