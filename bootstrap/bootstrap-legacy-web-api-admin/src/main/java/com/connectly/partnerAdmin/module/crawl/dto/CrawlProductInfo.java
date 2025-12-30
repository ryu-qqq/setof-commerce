package com.connectly.partnerAdmin.module.crawl.dto;

import com.connectly.partnerAdmin.module.external.enums.MappingStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrawlProductInfo {

    protected Long crawlProductId;
    protected long crawlProductSku;
    protected Long productGroupId;
    protected long brandId;
    protected long categoryId;
    protected long siteId;
    protected long sellerId;
    protected String colorCode;
    protected MappingStatus mappingStatus;

    @QueryProjection
    public CrawlProductInfo(Long crawlProductId, long crawlProductSku, Long productGroupId, long brandId, long categoryId, long siteId, long sellerId, String colorCode, MappingStatus mappingStatus) {
        this.crawlProductId = crawlProductId;
        this.crawlProductSku = crawlProductSku;
        this.productGroupId = productGroupId;
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.siteId = siteId;
        this.sellerId = sellerId;
        this.colorCode = colorCode;
        this.mappingStatus = mappingStatus;
    }
}
