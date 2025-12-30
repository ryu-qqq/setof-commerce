package com.connectly.partnerAdmin.module.category.filter;

import com.connectly.partnerAdmin.module.common.filter.LastDomainIdProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CategoryFilter implements LastDomainIdProvider {

    private Long lastDomainId;
    private Long categoryId;
    private String categoryName;
    private Integer categoryDepth;


    @Override
    public Long getId() {
        return lastDomainId;
    }

    @Override
    public boolean isNoOffsetFetch() {
        return lastDomainId != null;
    }

}
