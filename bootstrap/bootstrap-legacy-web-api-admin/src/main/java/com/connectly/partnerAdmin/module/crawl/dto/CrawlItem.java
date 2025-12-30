package com.connectly.partnerAdmin.module.crawl.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CrawlItem extends CrawlProductInfo{
    protected String sellerName;
    protected String brandName;
    private int stock;
    private BigDecimal price;
    private BigDecimal appPrice;
    private long discountRate;
}
