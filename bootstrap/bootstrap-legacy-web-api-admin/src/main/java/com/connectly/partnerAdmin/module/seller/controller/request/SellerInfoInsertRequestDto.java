package com.connectly.partnerAdmin.module.seller.controller.request;

import com.connectly.partnerAdmin.auth.enums.ApprovalStatus;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.seller.entity.Seller;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerInfoInsertRequestDto {

    private String sellerName;
    private String sellerLogoUrl;
    private String sellerDescription;
    private double commissionRate;
    private Yn privacyAgreementYn;
    private Yn termsUseAgreementYn;

    public Seller toSellerEntity(){
        return Seller.builder()
                .sellerName(sellerName)
                .sellerLogoUrl(sellerLogoUrl)
                .sellerDescription(sellerDescription)
                .commissionRate(commissionRate)
                .privacyAgreementYn(privacyAgreementYn)
                .termsUseAgreementYn(termsUseAgreementYn)
                .privacyAgreementDate(LocalDateTime.now())
                .termsUseAgreementDate(LocalDateTime.now())
                .approvalStatus(ApprovalStatus.PENDING)
                .build();
    }


}
