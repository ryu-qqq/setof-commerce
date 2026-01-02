package com.connectly.partnerAdmin.module.crawl.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrawlProductGroupInsertRequestDto {

    private String productId;
    private long itemNo;
    private String siteName;

    private String sellerId;
    private String name;
    private String brandName;
    private int price;
    private int normalPrice;
    private int originalPrice;
    private int discountRate;
    private int appPrice;
    private int appDiscountRate;

    private List<String> images;
    private String landingUrl;

    // Detailed info fields
    private int sellerNo;
    private String itemName;
    private String brandNameKr;
    private String brandCode;
    private Boolean isSoldOut;
    private int stock;
    private String descriptionMarkUp;

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

    // Options
    private  List<Option> options;




}
