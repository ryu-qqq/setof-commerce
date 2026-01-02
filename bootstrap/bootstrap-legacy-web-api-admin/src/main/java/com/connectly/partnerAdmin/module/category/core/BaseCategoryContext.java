package com.connectly.partnerAdmin.module.category.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseCategoryContext implements CategoryContext {
    protected long categoryId;
    protected String categoryName;
    protected String displayName;
    protected int categoryDepth;

    public BaseCategoryContext(long categoryId, String categoryName, String displayName, int categoryDepth) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.displayName = displayName;
        this.categoryDepth = categoryDepth;
    }
}
