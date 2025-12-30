package com.connectly.partnerAdmin.module.seller.entity;

import com.connectly.partnerAdmin.auth.enums.ApprovalStatus;
import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "SELLER")
@Entity
public class Seller extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    @Column(name = "SELLER_ID")
    private long id;
    private String sellerName;
    private String sellerLogoUrl;
    private String sellerDescription;
    private double commissionRate;
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;
    @Enumerated(EnumType.STRING)
    private Yn privacyAgreementYn;
    private LocalDateTime privacyAgreementDate;
    @Enumerated(EnumType.STRING)
    private Yn termsUseAgreementYn;
    private LocalDateTime termsUseAgreementDate;

    public void update(Seller seller){
        sellerName = seller.getSellerName();
        sellerLogoUrl =seller.getSellerLogoUrl();
        sellerDescription = seller.getSellerDescription();
        commissionRate = seller.getCommissionRate();
        privacyAgreementYn = seller.getPrivacyAgreementYn();
        termsUseAgreementYn = seller.getTermsUseAgreementYn();
    }

    public void updateStatus(ApprovalStatus approvalStatus){
        this.approvalStatus = approvalStatus;
    }

    //프론트 셀러 상세에서 업데이트 상태만 분리
    public void updateDetail(String sellerName, double commissionRate){
        this.sellerName = sellerName;
        this.commissionRate = commissionRate;
    }

}
