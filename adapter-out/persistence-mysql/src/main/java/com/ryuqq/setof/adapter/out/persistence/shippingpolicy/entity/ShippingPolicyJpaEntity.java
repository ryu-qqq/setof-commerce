package com.ryuqq.setof.adapter.out.persistence.shippingpolicy.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * ShippingPolicyJpaEntity - ShippingPolicy JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 seller_shipping_policies 테이블과 매핑됩니다.
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
@Table(name = "seller_shipping_policies")
public class ShippingPolicyJpaEntity extends SoftDeletableEntity {

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

    /** 기본 배송비 */
    @Column(name = "default_delivery_cost", nullable = false)
    private Integer defaultDeliveryCost;

    /** 무료배송 기준금액 */
    @Column(name = "free_shipping_threshold")
    private Integer freeShippingThreshold;

    /** 배송 안내 */
    @Column(name = "delivery_guide", columnDefinition = "TEXT")
    private String deliveryGuide;

    /** 기본 정책 여부 */
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;

    /** 표시 순서 */
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    /** JPA 기본 생성자 (protected) */
    protected ShippingPolicyJpaEntity() {
        // JPA 기본 생성자
    }

    /** 전체 필드 생성자 (private) */
    private ShippingPolicyJpaEntity(
            Long id,
            Long sellerId,
            String policyName,
            Integer defaultDeliveryCost,
            Integer freeShippingThreshold,
            String deliveryGuide,
            Boolean isDefault,
            Integer displayOrder,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.sellerId = sellerId;
        this.policyName = policyName;
        this.defaultDeliveryCost = defaultDeliveryCost;
        this.freeShippingThreshold = freeShippingThreshold;
        this.deliveryGuide = deliveryGuide;
        this.isDefault = isDefault;
        this.displayOrder = displayOrder;
    }

    /** of() 스태틱 팩토리 메서드 */
    public static ShippingPolicyJpaEntity of(
            Long id,
            Long sellerId,
            String policyName,
            Integer defaultDeliveryCost,
            Integer freeShippingThreshold,
            String deliveryGuide,
            Boolean isDefault,
            Integer displayOrder,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new ShippingPolicyJpaEntity(
                id,
                sellerId,
                policyName,
                defaultDeliveryCost,
                freeShippingThreshold,
                deliveryGuide,
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

    public Integer getDefaultDeliveryCost() {
        return defaultDeliveryCost;
    }

    public Integer getFreeShippingThreshold() {
        return freeShippingThreshold;
    }

    public String getDeliveryGuide() {
        return deliveryGuide;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }
}
