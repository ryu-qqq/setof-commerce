package com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity;

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
 * DiscountTargetJpaEntity - 할인 적용 대상 JPA 엔티티.
 *
 * <p>PER-ENT-001: Entity는 @Entity, @Table 어노테이션 필수.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션 금지.
 *
 * <p>PER-ENT-003: ID 필드는 @GeneratedValue(strategy = IDENTITY).
 *
 * <p>PER-ENT-004: Lombok 사용 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "discount_target")
public class DiscountTargetJpaEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discount_policy_id", nullable = false)
    private long discountPolicyId;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 20)
    private TargetType targetType;

    @Column(name = "target_id", nullable = false)
    private long targetId;

    @Column(name = "active", nullable = false)
    private boolean active;

    protected DiscountTargetJpaEntity() {
        super();
    }

    private DiscountTargetJpaEntity(
            Long id,
            long discountPolicyId,
            TargetType targetType,
            long targetId,
            boolean active,
            Instant createdAt,
            Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.discountPolicyId = discountPolicyId;
        this.targetType = targetType;
        this.targetId = targetId;
        this.active = active;
    }

    public static DiscountTargetJpaEntity create(
            Long id,
            long discountPolicyId,
            TargetType targetType,
            long targetId,
            boolean active,
            Instant createdAt,
            Instant updatedAt) {
        return new DiscountTargetJpaEntity(
                id, discountPolicyId, targetType, targetId, active, createdAt, updatedAt);
    }

    public Long getId() {
        return id;
    }

    public long getDiscountPolicyId() {
        return discountPolicyId;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public long getTargetId() {
        return targetId;
    }

    public boolean isActive() {
        return active;
    }

    /** Entity 내부 TargetType enum (domain DiscountTargetType과 1:1 매핑) */
    public enum TargetType {
        BRAND,
        SELLER,
        CATEGORY,
        PRODUCT
    }
}
