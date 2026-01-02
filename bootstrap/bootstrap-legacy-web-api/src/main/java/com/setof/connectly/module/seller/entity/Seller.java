package com.setof.connectly.module.seller.entity;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.common.enums.Yn;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "seller")
@Entity
public class Seller extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SELLER_ID")
    private long id;

    private String sellerName;

    private String sellerLogoUrl;

    private String sellerDescription;

    private double commissionRate;

    @Enumerated(EnumType.STRING)
    private Yn privacyAgreementYn;

    private LocalDateTime privacyAgreementDate;

    @Enumerated(EnumType.STRING)
    private Yn termsUseAgreementYn;

    private LocalDateTime termsUseAgreementDate;

    @OneToOne(
            mappedBy = "seller",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private SellerBusinessInfo sellerBusinessInfo;

    @OneToOne(
            mappedBy = "seller",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private SellerShippingInfo sellerShippingInfo;
}
