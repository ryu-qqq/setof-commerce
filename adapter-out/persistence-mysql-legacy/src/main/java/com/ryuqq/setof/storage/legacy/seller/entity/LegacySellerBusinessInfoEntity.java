package com.ryuqq.setof.storage.legacy.seller.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacySellerBusinessInfoEntity - 레거시 판매자 사업자 정보 엔티티.
 *
 * <p>레거시 DB의 seller_business_info 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "seller_business_info")
public class LegacySellerBusinessInfoEntity {

    @Id
    @Column(name = "seller_id")
    private Long id;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "business_address_line1")
    private String businessAddressLine1;

    @Column(name = "business_address_line2")
    private String businessAddressLine2;

    @Column(name = "business_address_zip_code")
    private String businessAddressZipCode;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_holder_name")
    private String accountHolderName;

    @Column(name = "cs_number")
    private String csNumber;

    @Column(name = "cs_phone_number")
    private String csPhoneNumber;

    @Column(name = "cs_email")
    private String csEmail;

    @Column(name = "sale_report_number")
    private String saleReportNumber;

    @Column(name = "representative")
    private String representative;

    protected LegacySellerBusinessInfoEntity() {}

    public Long getId() {
        return id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getBusinessAddressLine1() {
        return businessAddressLine1;
    }

    public String getBusinessAddressLine2() {
        return businessAddressLine2;
    }

    public String getBusinessAddressZipCode() {
        return businessAddressZipCode;
    }

    public String getBankName() {
        return bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getCsNumber() {
        return csNumber;
    }

    public String getCsPhoneNumber() {
        return csPhoneNumber;
    }

    public String getCsEmail() {
        return csEmail;
    }

    public String getSaleReportNumber() {
        return saleReportNumber;
    }

    public String getRepresentative() {
        return representative;
    }
}
