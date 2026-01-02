package com.connectly.partnerAdmin.module.seller.entity;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "SELLER_BUSINESS_INFO")
@Entity
public class SellerBusinessInfo extends BaseEntity {

    @Id
    @Column(name = "SELLER_ID")
    private long id;
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

    public void update(SellerBusinessInfo sellerBusinessInfo){
        registrationNumber = sellerBusinessInfo.getRegistrationNumber();
        companyName = sellerBusinessInfo.getCompanyName();
        businessAddressLine1 = sellerBusinessInfo.getBusinessAddressLine1();
        businessAddressLine2 = sellerBusinessInfo.getBusinessAddressLine2();
        businessAddressZipCode = sellerBusinessInfo.getBusinessAddressZipCode();
        bankName = sellerBusinessInfo.getBankName();
        accountNumber = sellerBusinessInfo.getAccountNumber();
        accountHolderName = sellerBusinessInfo.getAccountHolderName();
        csNumber = sellerBusinessInfo.getCsNumber();
        csPhoneNumber = sellerBusinessInfo.getCsPhoneNumber();
        csEmail = sellerBusinessInfo.getCsEmail();
        saleReportNumber = sellerBusinessInfo.getSaleReportNumber();
        representative = sellerBusinessInfo.getRepresentative();
    }
}
