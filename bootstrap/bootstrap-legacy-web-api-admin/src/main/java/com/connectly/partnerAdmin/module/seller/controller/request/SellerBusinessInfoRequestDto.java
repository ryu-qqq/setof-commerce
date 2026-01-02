package com.connectly.partnerAdmin.module.seller.controller.request;

import com.connectly.partnerAdmin.module.seller.entity.SellerBusinessInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerBusinessInfoRequestDto {

    private String registrationNumber;
    private String companyName;
    private String businessAddressLine1;
    private String businessAddressLine2;
    private String businessAddressZipCode;
    private String bankName;
    private String accountNumber;
    private String accountHolderName;
    private String csNumber;
    private String csPhoneNumber;
    private String csEmail;
    private String saleReportNumber;
    private String representative;

    public SellerBusinessInfo toSellerBusinessInfoEntity(long sellerId){
        return SellerBusinessInfo.builder()
                .id(sellerId)
                .registrationNumber(registrationNumber)
                .companyName(companyName)
                .businessAddressLine1(businessAddressLine1)
                .businessAddressLine2(businessAddressLine2)
                .businessAddressZipCode(businessAddressZipCode)
                .bankName(bankName)
                .accountNumber(accountNumber)
                .accountHolderName(accountHolderName)
                .csNumber(csNumber)
                .csPhoneNumber(csPhoneNumber)
                .csEmail(csEmail)
                .saleReportNumber(saleReportNumber)
                .representative(representative)
                .build();
    }

}
