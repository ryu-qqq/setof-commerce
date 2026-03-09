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
 * DiscountPolicyJpaEntity - 할인 정책 JPA 엔티티.
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
@Table(name = "discount_policy")
public class DiscountPolicyJpaEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_method", nullable = false, length = 20)
    private DiscountMethod discountMethod;

    @Column(name = "discount_rate")
    private Double discountRate;

    @Column(name = "discount_amount")
    private Integer discountAmount;

    @Column(name = "max_discount_amount")
    private Integer maxDiscountAmount;

    @Column(name = "discount_capped", nullable = false)
    private boolean discountCapped;

    @Column(name = "minimum_order_amount")
    private Integer minimumOrderAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "application_type", nullable = false, length = 20)
    private ApplicationType applicationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "publisher_type", nullable = false, length = 20)
    private PublisherType publisherType;

    @Column(name = "seller_id")
    private Long sellerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "stacking_group", nullable = false, length = 30)
    private StackingGroup stackingGroup;

    @Column(name = "priority", nullable = false)
    private int priority;

    @Column(name = "start_at", nullable = false)
    private Instant startAt;

    @Column(name = "end_at", nullable = false)
    private Instant endAt;

    @Column(name = "total_budget", nullable = false)
    private int totalBudget;

    @Column(name = "used_budget", nullable = false)
    private int usedBudget;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    protected DiscountPolicyJpaEntity() {
        super();
    }

    private DiscountPolicyJpaEntity(
            Long id,
            String name,
            String description,
            DiscountMethod discountMethod,
            Double discountRate,
            Integer discountAmount,
            Integer maxDiscountAmount,
            boolean discountCapped,
            Integer minimumOrderAmount,
            ApplicationType applicationType,
            PublisherType publisherType,
            Long sellerId,
            StackingGroup stackingGroup,
            int priority,
            Instant startAt,
            Instant endAt,
            int totalBudget,
            int usedBudget,
            boolean active,
            Instant deletedAt,
            Instant createdAt,
            Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.name = name;
        this.description = description;
        this.discountMethod = discountMethod;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.maxDiscountAmount = maxDiscountAmount;
        this.discountCapped = discountCapped;
        this.minimumOrderAmount = minimumOrderAmount;
        this.applicationType = applicationType;
        this.publisherType = publisherType;
        this.sellerId = sellerId;
        this.stackingGroup = stackingGroup;
        this.priority = priority;
        this.startAt = startAt;
        this.endAt = endAt;
        this.totalBudget = totalBudget;
        this.usedBudget = usedBudget;
        this.active = active;
        this.deletedAt = deletedAt;
    }

    public static DiscountPolicyJpaEntity create(
            Long id,
            String name,
            String description,
            DiscountMethod discountMethod,
            Double discountRate,
            Integer discountAmount,
            Integer maxDiscountAmount,
            boolean discountCapped,
            Integer minimumOrderAmount,
            ApplicationType applicationType,
            PublisherType publisherType,
            Long sellerId,
            StackingGroup stackingGroup,
            int priority,
            Instant startAt,
            Instant endAt,
            int totalBudget,
            int usedBudget,
            boolean active,
            Instant deletedAt,
            Instant createdAt,
            Instant updatedAt) {
        return new DiscountPolicyJpaEntity(
                id,
                name,
                description,
                discountMethod,
                discountRate,
                discountAmount,
                maxDiscountAmount,
                discountCapped,
                minimumOrderAmount,
                applicationType,
                publisherType,
                sellerId,
                stackingGroup,
                priority,
                startAt,
                endAt,
                totalBudget,
                usedBudget,
                active,
                deletedAt,
                createdAt,
                updatedAt);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public DiscountMethod getDiscountMethod() {
        return discountMethod;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public Integer getMaxDiscountAmount() {
        return maxDiscountAmount;
    }

    public boolean isDiscountCapped() {
        return discountCapped;
    }

    public Integer getMinimumOrderAmount() {
        return minimumOrderAmount;
    }

    public ApplicationType getApplicationType() {
        return applicationType;
    }

    public PublisherType getPublisherType() {
        return publisherType;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public StackingGroup getStackingGroup() {
        return stackingGroup;
    }

    public int getPriority() {
        return priority;
    }

    public Instant getStartAt() {
        return startAt;
    }

    public Instant getEndAt() {
        return endAt;
    }

    public int getTotalBudget() {
        return totalBudget;
    }

    public int getUsedBudget() {
        return usedBudget;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    /** Entity 내부 DiscountMethod enum (domain과 1:1 매핑) */
    public enum DiscountMethod {
        RATE,
        FIXED_AMOUNT
    }

    /** Entity 내부 ApplicationType enum (domain과 1:1 매핑) */
    public enum ApplicationType {
        IMMEDIATE,
        COUPON
    }

    /** Entity 내부 PublisherType enum (domain과 1:1 매핑) */
    public enum PublisherType {
        ADMIN,
        BRAND,
        SELLER
    }

    /** Entity 내부 StackingGroup enum (domain과 1:1 매핑) */
    public enum StackingGroup {
        SELLER_INSTANT,
        PLATFORM_INSTANT,
        COUPON
    }
}
