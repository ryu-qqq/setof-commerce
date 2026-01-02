package com.connectly.partnerAdmin.module.category.core;

import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExternalCategoryContext extends BaseCategoryContext {

    private long siteId;
    private SiteName siteName;
    private String mappingCategoryId;
    private String mappingCategoryDescription;

    @QueryProjection
    public ExternalCategoryContext(long categoryId, String categoryName, String displayName, int categoryDepth, long siteId, String mappingCategoryId, String mappingCategoryDescription) {
        super(categoryId, categoryName, displayName, categoryDepth);
        this.siteId = siteId;
        this.siteName = SiteName.of(siteId);
        this.mappingCategoryId = mappingCategoryId;
        this.mappingCategoryDescription =mappingCategoryDescription;
    }

}
