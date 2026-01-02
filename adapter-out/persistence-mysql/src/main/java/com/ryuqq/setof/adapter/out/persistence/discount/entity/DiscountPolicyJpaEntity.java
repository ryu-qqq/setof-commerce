package com.ryuqq.setof.adapter.out.persistence.discount.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * DiscountPolicyJpaEntity - DiscountPolicy JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 discount_policies 테이블과 매핑됩니다.
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
@Table(name = "discount_policies")
public class DiscountPolicyJpaEntity extends SoftDeletableEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 셀러 ID (Long FK) */
    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    /** 정책명 */
    @Column(name = "policy_name", nullable = false, length = 100)
    private String policyName;

    /** 할인 그룹 */
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_group", nullable = false, length = 30)
    private DiscountGroupType discountGroup;

    /** 할인 타입 (정률/정액) */
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false, length = 20)
    private DiscountTypeEnum discountType;

    /** 적용 대상 타입 */
    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 30)
    private DiscountTargetTypeEnum targetType;

    /** 적용 대상 IDs (JSON 또는 콤마 구분) */
    @Column(name = "target_ids", length = 1000)
    private String targetIds;

    /** 할인율 (정률 할인 시) */
    @Column(name = "discount_rate", precision = 5, scale = 2)
    private BigDecimal discountRate;

    /** 정액 할인 금액 */
    @Column(name = "discount_amount")
    private Long discountAmount;

    /** 최대 할인 금액 */
    @Column(name = "maximum_discount_amount")
    private Long maximumDiscountAmount;

    /** 최소 주문 금액 */
    @Column(name = "minimum_order_amount")
    private Long minimumOrderAmount;

    /** 유효 기간 시작일 */
    @Column(name = "valid_start_at", nullable = false)
    private Instant validStartAt;

    /** 유효 기간 종료일 */
    @Column(name = "valid_end_at", nullable = false)
    private Instant validEndAt;

    /** 고객별 최대 사용 횟수 */
    @Column(name = "max_usage_per_customer")
    private Integer maxUsagePerCustomer;

    /** 전체 최대 사용 횟수 */
    @Column(name = "max_total_usage")
    private Integer maxTotalUsage;

    /** 플랫폼 비용 분담 비율 */
    @Column(name = "platform_cost_share_ratio", precision = 5, scale = 2)
    private BigDecimal platformCostShareRatio;

    /** 셀러 비용 분담 비율 */
    @Column(name = "seller_cost_share_ratio", precision = 5, scale = 2)
    private BigDecimal sellerCostShareRatio;

    /** 우선순위 */
    @Column(name = "priority", nullable = false)
    private Integer priority;

    /** 활성화 여부 */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    /** JPA 기본 생성자 (protected) */
    protected DiscountPolicyJpaEntity() {
        // JPA 기본 생성자
    }

    /** 전체 필드 생성자 (private) */
    private DiscountPolicyJpaEntity(
            Long id,
            Long sellerId,
            String policyName,
            DiscountGroupType discountGroup,
            DiscountTypeEnum discountType,
            DiscountTargetTypeEnum targetType,
            String targetIds,
            BigDecimal discountRate,
            Long discountAmount,
            Long maximumDiscountAmount,
            Long minimumOrderAmount,
            Instant validStartAt,
            Instant validEndAt,
            Integer maxUsagePerCustomer,
            Integer maxTotalUsage,
            BigDecimal platformCostShareRatio,
            BigDecimal sellerCostShareRatio,
            Integer priority,
            Boolean isActive,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.sellerId = sellerId;
        this.policyName = policyName;
        this.discountGroup = discountGroup;
        this.discountType = discountType;
        this.targetType = targetType;
        this.targetIds = targetIds;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.maximumDiscountAmount = maximumDiscountAmount;
        this.minimumOrderAmount = minimumOrderAmount;
        this.validStartAt = validStartAt;
        this.validEndAt = validEndAt;
        this.maxUsagePerCustomer = maxUsagePerCustomer;
        this.maxTotalUsage = maxTotalUsage;
        this.platformCostShareRatio = platformCostShareRatio;
        this.sellerCostShareRatio = sellerCostShareRatio;
        this.priority = priority;
        this.isActive = isActive;
    }

    /** of() 스태틱 팩토리 메서드 */
    public static DiscountPolicyJpaEntity of(
            Long id,
            Long sellerId,
            String policyName,
            DiscountGroupType discountGroup,
            DiscountTypeEnum discountType,
            DiscountTargetTypeEnum targetType,
            String targetIds,
            BigDecimal discountRate,
            Long discountAmount,
            Long maximumDiscountAmount,
            Long minimumOrderAmount,
            Instant validStartAt,
            Instant validEndAt,
            Integer maxUsagePerCustomer,
            Integer maxTotalUsage,
            BigDecimal platformCostShareRatio,
            BigDecimal sellerCostShareRatio,
            Integer priority,
            Boolean isActive,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new DiscountPolicyJpaEntity(
                id,
                sellerId,
                policyName,
                discountGroup,
                discountType,
                targetType,
                targetIds,
                discountRate,
                discountAmount,
                maximumDiscountAmount,
                minimumOrderAmount,
                validStartAt,
                validEndAt,
                maxUsagePerCustomer,
                maxTotalUsage,
                platformCostShareRatio,
                sellerCostShareRatio,
                priority,
                isActive,
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

    public DiscountGroupType getDiscountGroup() {
        return discountGroup;
    }

    public DiscountTypeEnum getDiscountType() {
        return discountType;
    }

    public DiscountTargetTypeEnum getTargetType() {
        return targetType;
    }

    public String getTargetIds() {
        return targetIds;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public Long getDiscountAmount() {
        return discountAmount;
    }

    public Long getMaximumDiscountAmount() {
        return maximumDiscountAmount;
    }

    public Long getMinimumOrderAmount() {
        return minimumOrderAmount;
    }

    public Instant getValidStartAt() {
        return validStartAt;
    }

    public Instant getValidEndAt() {
        return validEndAt;
    }

    public Integer getMaxUsagePerCustomer() {
        return maxUsagePerCustomer;
    }

    public Integer getMaxTotalUsage() {
        return maxTotalUsage;
    }

    public BigDecimal getPlatformCostShareRatio() {
        return platformCostShareRatio;
    }

    public BigDecimal getSellerCostShareRatio() {
        return sellerCostShareRatio;
    }

    public Integer getPriority() {
        return priority;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    /** Persistence Layer Enum - DiscountGroup */
    public enum DiscountGroupType {
        CART,
        PRODUCT,
        CATEGORY,
        BRAND,
        MEMBER,
        EVENT,
        SHIPPING,
        PAYMENT,
        BUNDLE
    }

    /** Persistence Layer Enum - DiscountType */
    public enum DiscountTypeEnum {
        RATE,
        FIXED_PRICE
    }

    /** Persistence Layer Enum - DiscountTargetType */
    public enum DiscountTargetTypeEnum {
        ALL,
        PRODUCT,
        CATEGORY,
        BRAND,
        MEMBER_GRADE
    }
}
