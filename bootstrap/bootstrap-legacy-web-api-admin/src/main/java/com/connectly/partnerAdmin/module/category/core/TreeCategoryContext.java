package com.connectly.partnerAdmin.module.category.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TreeCategoryContext extends BaseCategoryContext {

    private long parentCategoryId;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<TreeCategoryContext> children = new ArrayList<>();

    @QueryProjection
    public TreeCategoryContext(long categoryId, String categoryName, String displayName, int categoryDepth, long parentCategoryId) {
        super(categoryId, categoryName, displayName, categoryDepth);
        this.parentCategoryId = parentCategoryId;
    }

}
