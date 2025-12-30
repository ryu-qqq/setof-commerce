package com.connectly.partnerAdmin.module.seller.controller.request;

import com.connectly.partnerAdmin.module.seller.entity.SellerShippingInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerShippingInfoRequestDto {

    private String returnAddressLine1;
    private String returnAddressLine2;
    private String returnAddressZipCode;

    public SellerShippingInfo toSellerShippingInfoEntity(long sellerId){
        return SellerShippingInfo.builder()
                .id(sellerId)
                .returnAddressLine1(returnAddressLine1)
                .returnAddressLine2(returnAddressLine2)
                .returnAddressZipCode(returnAddressZipCode)
                .build();
    }
}
