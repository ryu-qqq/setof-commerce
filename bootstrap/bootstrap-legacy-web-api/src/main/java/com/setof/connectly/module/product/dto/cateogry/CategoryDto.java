package com.setof.connectly.module.product.dto.cateogry;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.category.enums.TargetGroup;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryDto {

    private long categoryId;
    private String categoryName;
    private int categoryDepth;
    private TargetGroup targetGroup;
    private String categoryFullPath;

    @QueryProjection
    public CategoryDto(
            long categoryId,
            String categoryName,
            int categoryDepth,
            TargetGroup targetGroup,
            String categoryFullPath) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryDepth = categoryDepth;
        this.targetGroup = targetGroup;
        this.categoryFullPath = categoryFullPath;
    }
}
