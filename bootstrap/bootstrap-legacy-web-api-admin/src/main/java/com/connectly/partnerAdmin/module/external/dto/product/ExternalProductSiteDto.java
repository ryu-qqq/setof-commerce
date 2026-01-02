package com.connectly.partnerAdmin.module.external.dto.product;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExternalProductSiteDto {

    private long productGroupId;
    private long siteId;

    @QueryProjection
    public ExternalProductSiteDto(long productGroupId, long siteId) {
        this.productGroupId = productGroupId;
        this.siteId = siteId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExternalProductSiteDto that = (ExternalProductSiteDto) o;
        return productGroupId == that.productGroupId &&
                siteId == that.siteId;
    }

    @Override
    public int hashCode() {
        return(productGroupId + String.valueOf(siteId)).hashCode();
    }


}
