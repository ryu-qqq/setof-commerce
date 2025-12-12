package com.ryuqq.setof.adapter.out.persistence.seller.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * SellerJpaEntity - Seller JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 sellers 테이블과 매핑됩니다.
 *
 * <p><strong>SoftDeletableEntity 상속:</strong>
 *
 * <ul>
 *   <li>공통 감사 필드 상속: createdAt, updatedAt, deletedAt
 *   <li>Soft Delete 지원
 * </ul>
 *
 * <p><strong>Embedded 필드:</strong>
 *
 * <ul>
 *   <li>BusinessInfo: 사업자 정보 (registrationNumber, saleReportNumber, representative, address)
 * </ul>
 *
 * <p><strong>Lombok 금지:</strong>
 *
 * <ul>
 *   <li>Plain Java getter 사용
 *   <li>Setter 제공 금지
 *   <li>명시적 생성자 제공
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "sellers")
public class SellerJpaEntity extends SoftDeletableEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 셀러명 */
    @Column(name = "seller_name", nullable = false, length = 100)
    private String sellerName;

    /** 로고 URL */
    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    /** 셀러 설명 */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /** 승인 상태 (PENDING, APPROVED, REJECTED, SUSPENDED) */
    @Column(name = "approval_status", nullable = false, length = 20)
    private String approvalStatus;

    // ===== BusinessInfo Embedded 필드 =====

    /** 사업자등록번호 */
    @Column(name = "registration_number", nullable = false, length = 20)
    private String registrationNumber;

    /** 통신판매업 신고번호 */
    @Column(name = "sale_report_number", length = 50)
    private String saleReportNumber;

    /** 대표자명 */
    @Column(name = "representative", nullable = false, length = 50)
    private String representative;

    /** 사업장 주소 1 */
    @Column(name = "business_address_line1", nullable = false, length = 200)
    private String businessAddressLine1;

    /** 사업장 주소 2 */
    @Column(name = "business_address_line2", length = 100)
    private String businessAddressLine2;

    /** 사업장 우편번호 */
    @Column(name = "business_zip_code", nullable = false, length = 10)
    private String businessZipCode;

    /**
     * JPA 기본 생성자 (protected)
     *
     * <p>JPA 스펙 요구사항으로 반드시 필요합니다.
     */
    protected SellerJpaEntity() {
        // JPA 기본 생성자
    }

    /**
     * 전체 필드 생성자 (private)
     *
     * <p>직접 호출 금지, of() 스태틱 메서드로만 생성하세요.
     */
    private SellerJpaEntity(
            Long id,
            String sellerName,
            String logoUrl,
            String description,
            String approvalStatus,
            String registrationNumber,
            String saleReportNumber,
            String representative,
            String businessAddressLine1,
            String businessAddressLine2,
            String businessZipCode,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.sellerName = sellerName;
        this.logoUrl = logoUrl;
        this.description = description;
        this.approvalStatus = approvalStatus;
        this.registrationNumber = registrationNumber;
        this.saleReportNumber = saleReportNumber;
        this.representative = representative;
        this.businessAddressLine1 = businessAddressLine1;
        this.businessAddressLine2 = businessAddressLine2;
        this.businessZipCode = businessZipCode;
    }

    /**
     * of() 스태틱 팩토리 메서드 (Mapper 전용)
     *
     * <p>Entity 생성은 반드시 이 메서드를 통해서만 가능합니다.
     *
     * @param id Seller ID (null이면 신규 생성)
     * @param sellerName 셀러명
     * @param logoUrl 로고 URL
     * @param description 셀러 설명
     * @param approvalStatus 승인 상태
     * @param registrationNumber 사업자등록번호
     * @param saleReportNumber 통신판매업 신고번호
     * @param representative 대표자명
     * @param businessAddressLine1 사업장 주소 1
     * @param businessAddressLine2 사업장 주소 2
     * @param businessZipCode 사업장 우편번호
     * @param createdAt 생성 일시
     * @param updatedAt 수정 일시
     * @param deletedAt 삭제 일시
     * @return SellerJpaEntity 인스턴스
     */
    public static SellerJpaEntity of(
            Long id,
            String sellerName,
            String logoUrl,
            String description,
            String approvalStatus,
            String registrationNumber,
            String saleReportNumber,
            String representative,
            String businessAddressLine1,
            String businessAddressLine2,
            String businessZipCode,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new SellerJpaEntity(
                id,
                sellerName,
                logoUrl,
                description,
                approvalStatus,
                registrationNumber,
                saleReportNumber,
                representative,
                businessAddressLine1,
                businessAddressLine2,
                businessZipCode,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ===== Getters (Setter 제공 금지) =====

    public Long getId() {
        return id;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getSaleReportNumber() {
        return saleReportNumber;
    }

    public String getRepresentative() {
        return representative;
    }

    public String getBusinessAddressLine1() {
        return businessAddressLine1;
    }

    public String getBusinessAddressLine2() {
        return businessAddressLine2;
    }

    public String getBusinessZipCode() {
        return businessZipCode;
    }
}
