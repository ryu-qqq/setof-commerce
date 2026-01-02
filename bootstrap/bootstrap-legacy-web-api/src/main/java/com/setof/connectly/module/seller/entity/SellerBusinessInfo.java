package com.setof.connectly.module.seller.entity;

import com.setof.connectly.module.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "seller_business_info")
@Entity
public class SellerBusinessInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
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

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "seller_id")
    private Seller seller;

    public void setSeller(Seller seller) {
        this.seller = seller;
    }
}
