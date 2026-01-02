package com.connectly.partnerAdmin.module.seller.core;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusinessSellerContext extends BaseSellerContext {

    private String sellerName;
    private String logoUrl;
    private String sellerDescription;
    private String address;
    private String csPhoneNumber;
    private String alimTalkPhoneNumber;
    private String registrationNumber;
    private String saleReportNumber;
    private String representative;
    private double commissionRate;



    @QueryProjection
    public BusinessSellerContext(Long sellerId, String email, String sellerName, String logoUrl, String sellerDescription, String address, String csPhoneNumber, String alimTalkPhoneNumber, String registrationNumber, String saleReportNumber, String representative, double commissionRate) {
        super(sellerId, email);
        this.sellerName = sellerName;
        this.logoUrl = logoUrl;
        this.sellerDescription = sellerDescription;
        this.address = address;
        this.csPhoneNumber = csPhoneNumber;
        this.alimTalkPhoneNumber = alimTalkPhoneNumber;
        this.registrationNumber = registrationNumber;
        this.saleReportNumber = saleReportNumber;
        this.representative = representative;
        this.commissionRate = commissionRate;
    }



}
