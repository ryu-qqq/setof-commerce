package com.setof.connectly.module.category.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryDisplayDto {
    private long categoryId;
    private String categoryName;
    private int categoryDepth;
    private long parentCategoryId;

    private List<CategoryDisplayDto> children = new ArrayList<>();

    @QueryProjection
    public CategoryDisplayDto(
            long categoryId, String categoryName, long parentCategoryId, int categoryDepth) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.parentCategoryId = parentCategoryId;
        this.categoryDepth = categoryDepth;
    }
}
