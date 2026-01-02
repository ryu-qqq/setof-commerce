package com.connectly.partnerAdmin.module.crawl.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("product_detail_change")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDetailChangeRequestDto extends BaseProductRequestDto {

    private  String sellerNo;
    private  long sellerId;
    private  String itemName;
    private  String brandName;
    private  String brandNameKr;
    private  String brandCode;
    private  Boolean isSoldOut;
    private  Integer stock;

    // Category info
    private  String headerCategoryCode;
    private  String headerCategory;
    private  String largeCategoryCode;
    private  String largeCategory;
    private  String mediumCategoryCode;
    private  String mediumCategory;
    private  String smallCategoryCode;
    private  String smallCategory;

    // Shipping info
    private  String shippingTitle;
    private  List<ShippingItem> shippingItems;

    // Product details
    private  String itemStatus;
    private  String originCountry;
    private  String salesMethod;
    private  String descriptionMarkUp;

}
