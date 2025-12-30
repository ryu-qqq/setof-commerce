package com.connectly.partnerAdmin.module.crawl.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("mini_shop_change")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MiniShopChangeRequestDto extends BaseProductRequestDto {

    private  String sellerId;
    private  String name;
    private  String brandName;
    private  Double price;
    private  Double originalPrice;
    private  Double discountRate;
    private  Double appPrice;
    private  Double appDiscountRate;
    private  List<String> images;
    private  String landingUrl;


}
