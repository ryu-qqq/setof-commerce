package com.setof.connectly.module.common.filter;

import java.util.List;

public interface CategoryFilter {

    Long getCategoryId();

    List<Long> getCategoryIds();

    void setCategoryId(Long categoryId);

    void setCategoryIds(List<Long> categoryIds);
}
