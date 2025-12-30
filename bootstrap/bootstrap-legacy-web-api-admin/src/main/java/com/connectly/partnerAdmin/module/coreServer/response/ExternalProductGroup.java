package com.connectly.partnerAdmin.module.coreServer.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = DefaultExternalProductGroup.class, name = "default")
})
public interface ExternalProductGroup {

    long getSiteId();
    String getSiteName();
    long getSellerId();
    String getSellerName();
    long getProductGroupId();
    String getExternalProductGroupId();
    long getBrandId();
    String getExternalBrandId();
    long getCategoryId();
    String getExternalCategoryId();
    String getProductName();
    BigDecimal getRegularPrice();
    BigDecimal getCurrentPrice();
    String getStatus();
    boolean isFixedPrice();
    boolean isSoldOut();
    boolean isDisplayed();
}
