package com.ryuqq.setof.adapter.out.persistence.sellerapplication.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * SellerApplicationJpaEntity - 셀러 입점 신청 JPA 엔티티.
 *
 * <p>PER-ENT-001: Entity는 @Entity, @Table 어노테이션 필수.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션 금지 (@OneToMany, @ManyToOne 등).
 *
 * <p>PER-ENT-003: ID 필드는 @GeneratedValue(strategy = IDENTITY).
 *
 * <p>PER-ENT-004: Lombok 사용 금지 - 수동 Getter/생성자.
 */
@Entity
@Table(name = "seller_applications")
public class SellerApplicationJpaEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 신청자 기본 정보
    @Column(name = "seller_name", nullable = false, length = 100)
    private String sellerName;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "description", length = 2000)
    private String description;

    // 사업자 정보
    @Column(name = "registration_number", nullable = false, length = 20)
    private String registrationNumber;

    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;

    @Column(name = "representative", nullable = false, length = 50)
    private String representative;

    @Column(name = "sale_report_number", length = 50)
    private String saleReportNumber;

    @Column(name = "business_zip_code", nullable = false, length = 10)
    private String businessZipCode;

    @Column(name = "business_base_address", nullable = false, length = 200)
    private String businessBaseAddress;

    @Column(name = "business_detail_address", length = 200)
    private String businessDetailAddress;

    // CS 정보
    @Column(name = "cs_phone_number", nullable = false, length = 20)
    private String csPhoneNumber;

    @Column(name = "cs_email", nullable = false, length = 100)
    private String csEmail;

    // 주소 정보 (출고지/반품지)
    @Column(name = "address_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private AddressTypeJpaValue addressType;

    @Column(name = "address_name", nullable = false, length = 50)
    private String addressName;

    @Column(name = "address_zip_code", nullable = false, length = 10)
    private String addressZipCode;

    @Column(name = "address_base_address", nullable = false, length = 200)
    private String addressBaseAddress;

    @Column(name = "address_detail_address", length = 200)
    private String addressDetailAddress;

    @Column(name = "contact_name", nullable = false, length = 50)
    private String contactName;

    @Column(name = "contact_phone_number", nullable = false, length = 20)
    private String contactPhoneNumber;

    // 동의 정보
    @Column(name = "agreed_at", nullable = false)
    private Instant agreedAt;

    // 상태 관리
    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ApplicationStatusJpaValue status;

    @Column(name = "applied_at", nullable = false)
    private Instant appliedAt;

    @Column(name = "processed_at")
    private Instant processedAt;

    @Column(name = "processed_by", length = 100)
    private String processedBy;

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    // 승인 후 생성된 셀러 ID
    @Column(name = "approved_seller_id")
    private Long approvedSellerId;

    protected SellerApplicationJpaEntity() {
        super();
    }

    private SellerApplicationJpaEntity(
            Long id,
            String sellerName,
            String displayName,
            String logoUrl,
            String description,
            String registrationNumber,
            String companyName,
            String representative,
            String saleReportNumber,
            String businessZipCode,
            String businessBaseAddress,
            String businessDetailAddress,
            String csPhoneNumber,
            String csEmail,
            AddressTypeJpaValue addressType,
            String addressName,
            String addressZipCode,
            String addressBaseAddress,
            String addressDetailAddress,
            String contactName,
            String contactPhoneNumber,
            Instant agreedAt,
            ApplicationStatusJpaValue status,
            Instant appliedAt,
            Instant processedAt,
            String processedBy,
            String rejectionReason,
            Long approvedSellerId,
            Instant createdAt,
            Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.sellerName = sellerName;
        this.displayName = displayName;
        this.logoUrl = logoUrl;
        this.description = description;
        this.registrationNumber = registrationNumber;
        this.companyName = companyName;
        this.representative = representative;
        this.saleReportNumber = saleReportNumber;
        this.businessZipCode = businessZipCode;
        this.businessBaseAddress = businessBaseAddress;
        this.businessDetailAddress = businessDetailAddress;
        this.csPhoneNumber = csPhoneNumber;
        this.csEmail = csEmail;
        this.addressType = addressType;
        this.addressName = addressName;
        this.addressZipCode = addressZipCode;
        this.addressBaseAddress = addressBaseAddress;
        this.addressDetailAddress = addressDetailAddress;
        this.contactName = contactName;
        this.contactPhoneNumber = contactPhoneNumber;
        this.agreedAt = agreedAt;
        this.status = status;
        this.appliedAt = appliedAt;
        this.processedAt = processedAt;
        this.processedBy = processedBy;
        this.rejectionReason = rejectionReason;
        this.approvedSellerId = approvedSellerId;
    }

    public static SellerApplicationJpaEntity create(
            Long id,
            String sellerName,
            String displayName,
            String logoUrl,
            String description,
            String registrationNumber,
            String companyName,
            String representative,
            String saleReportNumber,
            String businessZipCode,
            String businessBaseAddress,
            String businessDetailAddress,
            String csPhoneNumber,
            String csEmail,
            AddressTypeJpaValue addressType,
            String addressName,
            String addressZipCode,
            String addressBaseAddress,
            String addressDetailAddress,
            String contactName,
            String contactPhoneNumber,
            Instant agreedAt,
            ApplicationStatusJpaValue status,
            Instant appliedAt,
            Instant processedAt,
            String processedBy,
            String rejectionReason,
            Long approvedSellerId,
            Instant createdAt,
            Instant updatedAt) {
        return new SellerApplicationJpaEntity(
                id,
                sellerName,
                displayName,
                logoUrl,
                description,
                registrationNumber,
                companyName,
                representative,
                saleReportNumber,
                businessZipCode,
                businessBaseAddress,
                businessDetailAddress,
                csPhoneNumber,
                csEmail,
                addressType,
                addressName,
                addressZipCode,
                addressBaseAddress,
                addressDetailAddress,
                contactName,
                contactPhoneNumber,
                agreedAt,
                status,
                appliedAt,
                processedAt,
                processedBy,
                rejectionReason,
                approvedSellerId,
                createdAt,
                updatedAt);
    }

    public Long getId() {
        return id;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getRepresentative() {
        return representative;
    }

    public String getSaleReportNumber() {
        return saleReportNumber;
    }

    public String getBusinessZipCode() {
        return businessZipCode;
    }

    public String getBusinessBaseAddress() {
        return businessBaseAddress;
    }

    public String getBusinessDetailAddress() {
        return businessDetailAddress;
    }

    public String getCsPhoneNumber() {
        return csPhoneNumber;
    }

    public String getCsEmail() {
        return csEmail;
    }

    public AddressTypeJpaValue getAddressType() {
        return addressType;
    }

    public String getAddressName() {
        return addressName;
    }

    public String getAddressZipCode() {
        return addressZipCode;
    }

    public String getAddressBaseAddress() {
        return addressBaseAddress;
    }

    public String getAddressDetailAddress() {
        return addressDetailAddress;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public Instant getAgreedAt() {
        return agreedAt;
    }

    public ApplicationStatusJpaValue getStatus() {
        return status;
    }

    public Instant getAppliedAt() {
        return appliedAt;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public String getProcessedBy() {
        return processedBy;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public Long getApprovedSellerId() {
        return approvedSellerId;
    }

    /** JPA Enum for AddressType. */
    public enum AddressTypeJpaValue {
        SHIPPING,
        RETURN
    }

    /** JPA Enum for ApplicationStatus. */
    public enum ApplicationStatusJpaValue {
        PENDING,
        APPROVED,
        REJECTED
    }
}
