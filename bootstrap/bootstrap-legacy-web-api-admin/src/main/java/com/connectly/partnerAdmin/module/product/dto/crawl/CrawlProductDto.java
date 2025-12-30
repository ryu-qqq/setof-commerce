package com.connectly.partnerAdmin.module.product.dto.crawl;

import com.connectly.partnerAdmin.module.external.enums.MappingStatus;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrawlProductDto {

    private String siteName;
    private long crawlProductSku;
    private String baseLinkUrl;
    private MappingStatus status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime insertDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;

    @QueryProjection
    public CrawlProductDto(long siteId, long crawlProductSku, MappingStatus status, LocalDateTime insertDate, LocalDateTime updateDate) {
        this.siteName = SiteName.of(siteId).getName();
        this.crawlProductSku = crawlProductSku;
        this.status = status;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
        this.baseLinkUrl = buildProductUrl(siteId, crawlProductSku);
    }

    public CrawlProductDto(String siteName, long crawlProductSku) {
        this.siteName = siteName;
        this.crawlProductSku = crawlProductSku;
        this.status = MappingStatus.PENDING;
        this.insertDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
        this.baseLinkUrl = "";
    }


    private String buildProductUrl(long siteId, long crawlProductSku){
        return String.format("%s%s",SiteName.of(siteId).getBaseLinkUrl(), crawlProductSku);
    }

}
