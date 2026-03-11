package com.ryuqq.setof.adapter.out.persistence.productgroup.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * ProductGroupAppliedDiscountJpaEntity - 상품그룹 적용 할인 내역 JPA 엔티티.
 *
 * <p>정산/감사용으로 어떤 할인 정책이 얼마의 할인을 제공했는지 기록합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "product_group_applied_discounts")
public class ProductGroupAppliedDiscountJpaEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_group_id", nullable = false)
    private Long productGroupId;

    @Column(name = "discount_policy_id", nullable = false)
    private Long discountPolicyId;

    @Column(name = "stacking_group", nullable = false, length = 50)
    private String stackingGroup;

    @Column(name = "discount_rate", nullable = false)
    private int discountRate;

    @Column(name = "discount_amount", nullable = false)
    private int discountAmount;

    @Column(name = "share_ratio", nullable = false, precision = 5, scale = 4)
    private BigDecimal shareRatio;

    @Column(name = "applied_at", nullable = false)
    private Instant appliedAt;

    protected ProductGroupAppliedDiscountJpaEntity() {
        super();
    }

    private ProductGroupAppliedDiscountJpaEntity(
            Long id,
            Long productGroupId,
            Long discountPolicyId,
            String stackingGroup,
            int discountRate,
            int discountAmount,
            BigDecimal shareRatio,
            Instant appliedAt,
            Instant createdAt,
            Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.productGroupId = productGroupId;
        this.discountPolicyId = discountPolicyId;
        this.stackingGroup = stackingGroup;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.shareRatio = shareRatio;
        this.appliedAt = appliedAt;
    }

    public static ProductGroupAppliedDiscountJpaEntity create(
            Long productGroupId,
            Long discountPolicyId,
            String stackingGroup,
            int discountRate,
            int discountAmount,
            BigDecimal shareRatio,
            Instant appliedAt,
            Instant createdAt,
            Instant updatedAt) {
        return new ProductGroupAppliedDiscountJpaEntity(
                null,
                productGroupId,
                discountPolicyId,
                stackingGroup,
                discountRate,
                discountAmount,
                shareRatio,
                appliedAt,
                createdAt,
                updatedAt);
    }

    public Long getId() {
        return id;
    }

    public Long getProductGroupId() {
        return productGroupId;
    }

    public Long getDiscountPolicyId() {
        return discountPolicyId;
    }

    public String getStackingGroup() {
        return stackingGroup;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getShareRatio() {
        return shareRatio;
    }

    public Instant getAppliedAt() {
        return appliedAt;
    }
}
