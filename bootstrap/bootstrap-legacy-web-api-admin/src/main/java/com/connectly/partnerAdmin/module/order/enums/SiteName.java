package com.connectly.partnerAdmin.module.order.enums;

import java.util.Arrays;

import com.connectly.partnerAdmin.module.common.enums.EnumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SiteName implements EnumType {

    OUR_MALL("SET_OF 몰",0,""),
    OCO("OCO",1L,""),
    SEWON("SELLIC",2L,""),
    SHEIN("SHEIN",3L,""),
    NAVER("NAVER",4L,""),
    MUSTIT("MUSTIT",5L,"https://mustit.co.kr/product/product_detail/"),
    KREAM("KREAM",6L,"https://kream.co.kr/products/"),
    KASINA("KASINA",7L, "https://www.kasina.co.kr/product-detail/"),
    BUYMA("BUYMA",8L, ""),
    LF("LF",9L, ""),
    ;

    private final String displayName;
    private final long siteId;
    private final String baseLinkUrl;

    public static SiteName of(long siteId) {
        return Arrays.stream(SiteName.values())
                .filter(r -> r.getSiteId() == siteId)
                .findAny()
                .orElseGet(()-> SiteName.OUR_MALL);
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return name();
    }

    public boolean isOurMall(){
        return this.equals(OUR_MALL);
    }




}
