package com.connectly.partnerAdmin.module.crawl.dto;

import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerCrawlSiteRelationDto {

    private long siteId;
    private SiteName siteName;
    private long sellerId;
    private String sellerName;
    private String baseUrl;
    private String endPointUrl;
    private String parameters;

    @QueryProjection
    public SellerCrawlSiteRelationDto(long siteId, long sellerId, String sellerName, String baseUrl, String endPointUrl, String parameters) {
        this.siteId = siteId;
        this.siteName = SiteName.of(siteId);
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.baseUrl = baseUrl;
        this.endPointUrl = endPointUrl;
        this.parameters = parameters;
    }

}
