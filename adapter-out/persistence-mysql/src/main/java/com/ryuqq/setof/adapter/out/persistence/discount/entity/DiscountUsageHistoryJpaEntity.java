package com.ryuqq.setof.adapter.out.persistence.discount.entity;

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
 * DiscountUsageHistoryJpaEntity - 할인 사용 이력 JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 discount_usage_histories 테이블과 매핑됩니다.
 *
 * <p><strong>BaseAuditEntity 상속:</strong>
 *
 * <ul>
 *   <li>공통 감사 필드 상속: createdAt, updatedAt
 *   <li>할인 사용 이력은 Soft Delete 미사용 (영구 보관)
 * </ul>
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>discountPolicyId: Long 타입으로 FK 관리
 *   <li>JPA 관계 어노테이션 사용 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "discount_usage_histories")
public class DiscountUsageHistoryJpaEntity extends BaseAuditEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 할인 정책 ID (Long FK) */
    @Column(name = "discount_policy_id", nullable = false)
    private Long discountPolicyId;

    /** 회원 ID (UUIDv7 String) */
    @Column(name = "member_id", nullable = false, length = 36)
    private String memberId;

    /** 결제 세션 ID (UUID String) */
    @Column(name = "checkout_id", nullable = false, length = 36)
    private String checkoutId;

    /** 주문 ID (UUID String) */
    @Column(name = "order_id", nullable = false, length = 36)
    private String orderId;

    /** 실제 적용된 할인 금액 (원) */
    @Column(name = "applied_amount", nullable = false)
    private Long appliedAmount;

    /** 할인 전 원래 금액 (원) */
    @Column(name = "original_amount", nullable = false)
    private Long originalAmount;

    /** 플랫폼 비용 분담 비율 스냅샷 */
    @Column(name = "platform_ratio", nullable = false, precision = 5, scale = 2)
    private BigDecimal platformRatio;

    /** 셀러 비용 분담 비율 스냅샷 */
    @Column(name = "seller_ratio", nullable = false, precision = 5, scale = 2)
    private BigDecimal sellerRatio;

    /** 플랫폼 부담 금액 (계산된 값) */
    @Column(name = "platform_cost", nullable = false)
    private Long platformCost;

    /** 셀러 부담 금액 (계산된 값) */
    @Column(name = "seller_cost", nullable = false)
    private Long sellerCost;

    /** 할인 사용 시점 */
    @Column(name = "used_at", nullable = false)
    private Instant usedAt;

    /** JPA 기본 생성자 (protected) */
    protected DiscountUsageHistoryJpaEntity() {
        // JPA 기본 생성자
    }

    /** 전체 필드 생성자 (private) */
    private DiscountUsageHistoryJpaEntity(
            Long id,
            Long discountPolicyId,
            String memberId,
            String checkoutId,
            String orderId,
            Long appliedAmount,
            Long originalAmount,
            BigDecimal platformRatio,
            BigDecimal sellerRatio,
            Long platformCost,
            Long sellerCost,
            Instant usedAt,
            Instant createdAt,
            Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.discountPolicyId = discountPolicyId;
        this.memberId = memberId;
        this.checkoutId = checkoutId;
        this.orderId = orderId;
        this.appliedAmount = appliedAmount;
        this.originalAmount = originalAmount;
        this.platformRatio = platformRatio;
        this.sellerRatio = sellerRatio;
        this.platformCost = platformCost;
        this.sellerCost = sellerCost;
        this.usedAt = usedAt;
    }

    /** of() 스태틱 팩토리 메서드 */
    public static DiscountUsageHistoryJpaEntity of(
            Long id,
            Long discountPolicyId,
            String memberId,
            String checkoutId,
            String orderId,
            Long appliedAmount,
            Long originalAmount,
            BigDecimal platformRatio,
            BigDecimal sellerRatio,
            Long platformCost,
            Long sellerCost,
            Instant usedAt,
            Instant createdAt,
            Instant updatedAt) {
        return new DiscountUsageHistoryJpaEntity(
                id,
                discountPolicyId,
                memberId,
                checkoutId,
                orderId,
                appliedAmount,
                originalAmount,
                platformRatio,
                sellerRatio,
                platformCost,
                sellerCost,
                usedAt,
                createdAt,
                updatedAt);
    }

    // ===== Getters =====

    public Long getId() {
        return id;
    }

    public Long getDiscountPolicyId() {
        return discountPolicyId;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getCheckoutId() {
        return checkoutId;
    }

    public String getOrderId() {
        return orderId;
    }

    public Long getAppliedAmount() {
        return appliedAmount;
    }

    public Long getOriginalAmount() {
        return originalAmount;
    }

    public BigDecimal getPlatformRatio() {
        return platformRatio;
    }

    public BigDecimal getSellerRatio() {
        return sellerRatio;
    }

    public Long getPlatformCost() {
        return platformCost;
    }

    public Long getSellerCost() {
        return sellerCost;
    }

    public Instant getUsedAt() {
        return usedAt;
    }
}
