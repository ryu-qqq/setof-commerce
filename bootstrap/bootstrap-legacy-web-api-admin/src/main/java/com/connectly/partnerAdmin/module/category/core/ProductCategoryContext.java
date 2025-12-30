package com.connectly.partnerAdmin.module.category.core;

import com.connectly.partnerAdmin.module.category.enums.TargetGroup;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCategoryContext  extends BaseCategoryContext{

    private TargetGroup targetGroup;
    private String categoryFullPath;

    @QueryProjection
    public ProductCategoryContext(long categoryId, String categoryName, String displayName, int categoryDepth, String categoryFullPath, TargetGroup targetGroup) {
        super(categoryId, categoryName, displayName, categoryDepth);
        this.categoryFullPath = categoryFullPath;
        this.targetGroup = targetGroup;
    }

}
