package com.connectly.partnerAdmin.module.seller.controller.request;
import com.connectly.partnerAdmin.module.seller.entity.SellerBusinessInfo;
import com.connectly.partnerAdmin.module.seller.entity.SellerShippingInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerUpdateDetailRequestDto {
    private String sellerName;
    private String csEmail;
    private String csNumber;
    private double commissionRate;
    private String returnAddressLine1;
    private String returnAddressLine2;
    private String returnAddressZipCode;
    private List<Long> siteIds;

    public SellerShippingInfo toSellerShippingInfoEntity(long sellerId){
        return SellerShippingInfo.builder()
                .id(sellerId)
                .returnAddressLine1(returnAddressLine1)
                .returnAddressLine2(returnAddressLine2)
                .returnAddressZipCode(returnAddressZipCode)
                .build();
    }

    public SellerBusinessInfo toSellerBusinessInfoEntity(SellerBusinessInfo originBusinessInfo) {
        return SellerBusinessInfo.builder()
                .id(originBusinessInfo.getId())
                .registrationNumber(originBusinessInfo.getRegistrationNumber())
                .companyName(originBusinessInfo.getCompanyName())
                .businessAddressLine1(originBusinessInfo.getBusinessAddressLine1())
                .businessAddressLine2(originBusinessInfo.getBusinessAddressLine2())
                .businessAddressZipCode(originBusinessInfo.getBusinessAddressZipCode())
                .bankName(originBusinessInfo.getBankName())
                .accountNumber(originBusinessInfo.getAccountNumber())
                .accountHolderName(originBusinessInfo.getAccountHolderName())
                .csNumber(csNumber)
                .csPhoneNumber(originBusinessInfo.getCsPhoneNumber())
                .csEmail(csEmail)
                .saleReportNumber(originBusinessInfo.getSaleReportNumber())
                .representative(originBusinessInfo.getRepresentative())
                .build();
    }

}
