package com.connectly.partnerAdmin.module.seller.controller.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerInfoContextRequestDto {
    private SellerInfoInsertRequestDto sellerInfo;
    private SellerBusinessInfoRequestDto sellerBusinessInfo;
    private SellerShippingInfoRequestDto sellerShippingInfo;
}
