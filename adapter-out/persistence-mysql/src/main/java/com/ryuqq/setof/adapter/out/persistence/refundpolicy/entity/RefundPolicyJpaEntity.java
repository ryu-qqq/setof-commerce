package com.ryuqq.setof.adapter.out.persistence.refundpolicy.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * RefundPolicyJpaEntity - RefundPolicy JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 seller_refund_policies 테이블과 매핑됩니다.
 *
 * <p><strong>SoftDeletableEntity 상속:</strong>
 *
 * <ul>
 *   <li>공통 감사 필드 상속: createdAt, updatedAt, deletedAt
 *   <li>Soft Delete 지원
 * </ul>
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>sellerId: Long 타입으로 FK 관리
 *   <li>JPA 관계 어노테이션 사용 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "seller_refund_policies")
public class RefundPolicyJpaEntity extends SoftDeletableEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 셀러 ID (Long FK) */
    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    /** 정책명 */
    @Column(name = "policy_name", nullable = false, length = 50)
    private String policyName;

    /** 반품 주소 1 */
    @Column(name = "return_address_line1", nullable = false, length = 200)
    private String returnAddressLine1;

    /** 반품 주소 2 */
    @Column(name = "return_address_line2", length = 100)
    private String returnAddressLine2;

    /** 반품 우편번호 */
    @Column(name = "return_zip_code", nullable = false, length = 10)
    private String returnZipCode;

    /** 환불 기간 (일) */
    @Column(name = "refund_period_days", nullable = false)
    private Integer refundPeriodDays;

    /** 환불 배송비 */
    @Column(name = "refund_delivery_cost", nullable = false)
    private Integer refundDeliveryCost;

    /** 환불 안내 */
    @Column(name = "refund_guide", columnDefinition = "TEXT")
    private String refundGuide;

    /** 기본 정책 여부 */
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;

    /** 표시 순서 */
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    /** JPA 기본 생성자 (protected) */
    protected RefundPolicyJpaEntity() {
        // JPA 기본 생성자
    }

    /** 전체 필드 생성자 (private) */
    private RefundPolicyJpaEntity(
            Long id,
            Long sellerId,
            String policyName,
            String returnAddressLine1,
            String returnAddressLine2,
            String returnZipCode,
            Integer refundPeriodDays,
            Integer refundDeliveryCost,
            String refundGuide,
            Boolean isDefault,
            Integer displayOrder,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.sellerId = sellerId;
        this.policyName = policyName;
        this.returnAddressLine1 = returnAddressLine1;
        this.returnAddressLine2 = returnAddressLine2;
        this.returnZipCode = returnZipCode;
        this.refundPeriodDays = refundPeriodDays;
        this.refundDeliveryCost = refundDeliveryCost;
        this.refundGuide = refundGuide;
        this.isDefault = isDefault;
        this.displayOrder = displayOrder;
    }

    /** of() 스태틱 팩토리 메서드 */
    public static RefundPolicyJpaEntity of(
            Long id,
            Long sellerId,
            String policyName,
            String returnAddressLine1,
            String returnAddressLine2,
            String returnZipCode,
            Integer refundPeriodDays,
            Integer refundDeliveryCost,
            String refundGuide,
            Boolean isDefault,
            Integer displayOrder,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new RefundPolicyJpaEntity(
                id,
                sellerId,
                policyName,
                returnAddressLine1,
                returnAddressLine2,
                returnZipCode,
                refundPeriodDays,
                refundDeliveryCost,
                refundGuide,
                isDefault,
                displayOrder,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ===== Getters =====

    public Long getId() {
        return id;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public String getPolicyName() {
        return policyName;
    }

    public String getReturnAddressLine1() {
        return returnAddressLine1;
    }

    public String getReturnAddressLine2() {
        return returnAddressLine2;
    }

    public String getReturnZipCode() {
        return returnZipCode;
    }

    public Integer getRefundPeriodDays() {
        return refundPeriodDays;
    }

    public Integer getRefundDeliveryCost() {
        return refundDeliveryCost;
    }

    public String getRefundGuide() {
        return refundGuide;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }
}
