package com.connectly.partnerAdmin.module.brand.core;

import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExternalBrandContext extends BaseBrandContext {

    private long siteId;
    private SiteName siteName;
    private String mappingBrandId;

    @QueryProjection
    public ExternalBrandContext(long brandId, String brandName, long siteId, String mappingBrandId) {
        super(brandId, brandName);
        this.siteId = siteId;
        this.siteName = SiteName.of(siteId);
        this.mappingBrandId = mappingBrandId;
    }

}
