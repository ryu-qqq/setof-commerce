package com.connectly.partnerAdmin.module.crawl.dto.request;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Base DTO for all product export requests.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "dtoType",
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = CrawlProductGroupInsertRequestDto.class, name = "crawlProductGroupInsertRequestDto"),
    @JsonSubTypes.Type(value = MiniShopChangeRequestDto.class, name = "mini_shop_change"),
    @JsonSubTypes.Type(value = ProductDetailChangeRequestDto.class, name = "product_detail_change"),
    @JsonSubTypes.Type(value = ProductOptionsChangeRequestDto.class, name = "product_options_change")
})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public abstract class BaseProductRequestDto {
    public String dtoType;
    public String productId;
    public long itemNo;
    public String siteName;
    public String changeTimestamp;
    public String exportTimestamp;

    public BaseProductRequestDto() {}



}
