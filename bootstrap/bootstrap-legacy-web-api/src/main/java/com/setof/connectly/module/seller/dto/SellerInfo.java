package com.setof.connectly.module.seller.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerInfo {

    private long sellerId;
    private String sellerName;
    private String logoUrl;
    private String sellerDescription;
    private String address;
    private String csPhoneNumber;
    private String alimTalkPhoneNumber;
    private String registrationNumber;
    private String saleReportNumber;
    private String representative;
    private String email;

    @QueryProjection
    public SellerInfo(long sellerId, String sellerName, String logoUrl, String sellerDescription) {
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.logoUrl = logoUrl;
        this.sellerDescription = sellerDescription;
    }

    @QueryProjection
    public SellerInfo(
            long sellerId,
            String sellerName,
            String logoUrl,
            String sellerDescription,
            String address,
            String csPhoneNumber,
            String alimTalkPhoneNumber,
            String registrationNumber,
            String saleReportNumber,
            String representative,
            String email) {
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.logoUrl = logoUrl;
        this.sellerDescription = sellerDescription;
        this.address = address;
        this.csPhoneNumber = csPhoneNumber;
        this.alimTalkPhoneNumber = alimTalkPhoneNumber;
        this.registrationNumber = registrationNumber;
        this.saleReportNumber = saleReportNumber;
        this.representative = representative;
        this.email = email;
    }
}
