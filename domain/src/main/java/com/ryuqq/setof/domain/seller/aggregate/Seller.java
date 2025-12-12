package com.ryuqq.setof.domain.seller.aggregate;

import com.ryuqq.setof.domain.seller.vo.ApprovalStatus;
import com.ryuqq.setof.domain.seller.vo.BusinessInfo;
import com.ryuqq.setof.domain.seller.vo.CustomerServiceInfo;
import com.ryuqq.setof.domain.seller.vo.Description;
import com.ryuqq.setof.domain.seller.vo.LogoUrl;
import com.ryuqq.setof.domain.seller.vo.SellerId;
import com.ryuqq.setof.domain.seller.vo.SellerName;
import java.time.Instant;

/**
 * Seller Aggregate Root
 *
 * <p>셀러 정보를 나타내는 도메인 엔티티입니다. 마켓플레이스에서 Admin API를 통해 Push됩니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - final 필드
 *   <li>Private 생성자 + Static Factory - 외부 직접 생성 금지
 *   <li>Law of Demeter - Helper 메서드로 내부 객체 접근 제공
 * </ul>
 */
public class Seller {

    private final SellerId id;
    private final SellerName name;
    private final LogoUrl logoUrl;
    private final Description description;
    private final ApprovalStatus approvalStatus;
    private final BusinessInfo businessInfo;
    private final CustomerServiceInfo customerServiceInfo;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant deletedAt;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private Seller(
            SellerId id,
            SellerName name,
            LogoUrl logoUrl,
            Description description,
            ApprovalStatus approvalStatus,
            BusinessInfo businessInfo,
            CustomerServiceInfo customerServiceInfo,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.description = description;
        this.approvalStatus = approvalStatus;
        this.businessInfo = businessInfo;
        this.customerServiceInfo = customerServiceInfo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    /**
     * 신규 셀러 생성용 Static Factory Method
     *
     * <p>ID 없이 신규 생성, 상태는 PENDING으로 시작
     *
     * @param name 셀러 이름
     * @param logoUrl 로고 URL (nullable)
     * @param description 셀러 설명 (nullable)
     * @param businessInfo 사업자 정보
     * @param customerServiceInfo 고객 서비스 정보
     * @param createdAt 생성일시
     * @return Seller 인스턴스
     */
    public static Seller create(
            SellerName name,
            LogoUrl logoUrl,
            Description description,
            BusinessInfo businessInfo,
            CustomerServiceInfo customerServiceInfo,
            Instant createdAt) {
        return new Seller(
                null,
                name,
                logoUrl,
                description,
                ApprovalStatus.PENDING,
                businessInfo,
                customerServiceInfo,
                createdAt,
                createdAt,
                null);
    }

    /**
     * Persistence에서 복원용 Static Factory Method
     *
     * <p>검증 없이 모든 필드를 그대로 복원
     *
     * @param id 셀러 ID
     * @param name 셀러 이름
     * @param logoUrl 로고 URL (nullable)
     * @param description 셀러 설명 (nullable)
     * @param approvalStatus 승인 상태
     * @param businessInfo 사업자 정보
     * @param customerServiceInfo 고객 서비스 정보
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @param deletedAt 삭제일시 (nullable)
     * @return Seller 인스턴스
     */
    public static Seller reconstitute(
            SellerId id,
            SellerName name,
            LogoUrl logoUrl,
            Description description,
            ApprovalStatus approvalStatus,
            BusinessInfo businessInfo,
            CustomerServiceInfo customerServiceInfo,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new Seller(
                id,
                name,
                logoUrl,
                description,
                approvalStatus,
                businessInfo,
                customerServiceInfo,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ========== 비즈니스 메서드 ==========

    /**
     * 셀러 정보 업데이트
     *
     * @param name 새로운 셀러 이름
     * @param logoUrl 새로운 로고 URL
     * @param description 새로운 셀러 설명
     * @param businessInfo 새로운 사업자 정보
     * @param customerServiceInfo 새로운 고객 서비스 정보
     * @param updatedAt 수정일시
     * @return 업데이트된 Seller 인스턴스
     */
    public Seller update(
            SellerName name,
            LogoUrl logoUrl,
            Description description,
            BusinessInfo businessInfo,
            CustomerServiceInfo customerServiceInfo,
            Instant updatedAt) {
        return new Seller(
                this.id,
                name,
                logoUrl,
                description,
                this.approvalStatus,
                businessInfo,
                customerServiceInfo,
                this.createdAt,
                updatedAt,
                this.deletedAt);
    }

    /**
     * 승인 상태 변경
     *
     * @param newStatus 새로운 승인 상태
     * @param updatedAt 수정일시
     * @return 상태가 변경된 Seller 인스턴스
     */
    public Seller changeApprovalStatus(ApprovalStatus newStatus, Instant updatedAt) {
        return new Seller(
                this.id,
                this.name,
                this.logoUrl,
                this.description,
                newStatus,
                this.businessInfo,
                this.customerServiceInfo,
                this.createdAt,
                updatedAt,
                this.deletedAt);
    }

    /**
     * 승인 처리
     *
     * @param updatedAt 수정일시
     * @return 승인된 Seller 인스턴스
     * @throws IllegalStateException PENDING 상태가 아닌 경우
     */
    public Seller approve(Instant updatedAt) {
        if (!approvalStatus.canBeApproved()) {
            throw new IllegalStateException(
                    String.format("현재 상태(%s)에서는 승인할 수 없습니다.", approvalStatus));
        }
        return changeApprovalStatus(ApprovalStatus.APPROVED, updatedAt);
    }

    /**
     * 거부 처리
     *
     * @param updatedAt 수정일시
     * @return 거부된 Seller 인스턴스
     * @throws IllegalStateException PENDING 상태가 아닌 경우
     */
    public Seller reject(Instant updatedAt) {
        if (!approvalStatus.canBeApproved()) {
            throw new IllegalStateException(
                    String.format("현재 상태(%s)에서는 거부할 수 없습니다.", approvalStatus));
        }
        return changeApprovalStatus(ApprovalStatus.REJECTED, updatedAt);
    }

    /**
     * 정지 처리
     *
     * @param updatedAt 수정일시
     * @return 정지된 Seller 인스턴스
     * @throws IllegalStateException APPROVED 상태가 아닌 경우
     */
    public Seller suspend(Instant updatedAt) {
        if (!approvalStatus.canBeSuspended()) {
            throw new IllegalStateException(
                    String.format("현재 상태(%s)에서는 정지할 수 없습니다.", approvalStatus));
        }
        return changeApprovalStatus(ApprovalStatus.SUSPENDED, updatedAt);
    }

    /**
     * 삭제 처리 (Soft Delete)
     *
     * @param deletedAt 삭제일시
     * @return 삭제된 Seller 인스턴스
     */
    public Seller delete(Instant deletedAt) {
        return new Seller(
                this.id,
                this.name,
                this.logoUrl,
                this.description,
                this.approvalStatus,
                this.businessInfo,
                this.customerServiceInfo,
                this.createdAt,
                deletedAt,
                deletedAt);
    }

    /**
     * 활성 상태 여부 확인 (Tell, Don't Ask)
     *
     * @return APPROVED 상태이면 true
     */
    public boolean isActive() {
        return approvalStatus.isActive();
    }

    /**
     * 삭제 여부 확인
     *
     * @return 삭제되었으면 true
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }

    /**
     * ID 존재 여부 확인 (영속화 여부)
     *
     * @return ID가 있으면 true
     */
    public boolean hasId() {
        return id != null;
    }

    // ========== Law of Demeter Helper Methods ==========

    /**
     * 셀러 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 셀러 ID Long 값, ID가 없으면 null
     */
    public Long getIdValue() {
        return id != null ? id.value() : null;
    }

    /**
     * 셀러 이름 값 반환 (Law of Demeter 준수)
     *
     * @return 셀러 이름 문자열
     */
    public String getNameValue() {
        return name.value();
    }

    /**
     * 로고 URL 값 반환 (Law of Demeter 준수)
     *
     * @return 로고 URL 문자열 (nullable)
     */
    public String getLogoUrlValue() {
        return logoUrl != null ? logoUrl.value() : null;
    }

    /**
     * 셀러 설명 값 반환 (Law of Demeter 준수)
     *
     * @return 셀러 설명 문자열 (nullable)
     */
    public String getDescriptionValue() {
        return description != null ? description.value() : null;
    }

    /**
     * 승인 상태 이름 반환 (Law of Demeter 준수)
     *
     * @return 승인 상태 문자열
     */
    public String getApprovalStatusValue() {
        return approvalStatus.name();
    }

    // ========== 사업자 정보 Helper Methods ==========

    public String getRegistrationNumber() {
        return businessInfo != null ? businessInfo.getRegistrationNumberValue() : null;
    }

    public String getSaleReportNumber() {
        return businessInfo != null ? businessInfo.getSaleReportNumberValue() : null;
    }

    public String getRepresentative() {
        return businessInfo != null ? businessInfo.getRepresentativeValue() : null;
    }

    public String getBusinessAddressLine1() {
        return businessInfo != null ? businessInfo.getAddressLine1() : null;
    }

    public String getBusinessAddressLine2() {
        return businessInfo != null ? businessInfo.getAddressLine2() : null;
    }

    public String getBusinessZipCode() {
        return businessInfo != null ? businessInfo.getZipCode() : null;
    }

    // ========== CS 정보 Helper Methods ==========

    public String getCsEmail() {
        return customerServiceInfo != null ? customerServiceInfo.getEmailValue() : null;
    }

    public String getCsMobilePhone() {
        return customerServiceInfo != null ? customerServiceInfo.getMobilePhoneValue() : null;
    }

    public String getCsLandlinePhone() {
        return customerServiceInfo != null ? customerServiceInfo.getLandlinePhoneValue() : null;
    }

    // ========== Getter 메서드 (Lombok 금지) ==========

    public SellerId getId() {
        return id;
    }

    public SellerName getName() {
        return name;
    }

    public LogoUrl getLogoUrl() {
        return logoUrl;
    }

    public Description getDescription() {
        return description;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public BusinessInfo getBusinessInfo() {
        return businessInfo;
    }

    public CustomerServiceInfo getCustomerServiceInfo() {
        return customerServiceInfo;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
