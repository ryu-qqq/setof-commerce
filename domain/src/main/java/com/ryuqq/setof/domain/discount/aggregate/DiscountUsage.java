package com.ryuqq.setof.domain.discount.aggregate;

import com.ryuqq.setof.domain.discount.id.DiscountPolicyId;
import com.ryuqq.setof.domain.discount.id.DiscountUsageId;
import java.time.Instant;

/**
 * DiscountUsage - 할인 사용 이력 Aggregate.
 *
 * <p>주문 확정 시 실제 할인이 적용된 기록을 저장합니다. 아웃박스 → SQS → Consumer 흐름으로 비동기 저장됩니다.
 *
 * <p>주요 불변식:
 *
 * <ul>
 *   <li>discountAmount는 0보다 커야 함
 *   <li>originalPrice ≥ discountAmount
 *   <li>finalPrice = originalPrice - discountAmount
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class DiscountUsage {

    private final DiscountUsageId id;
    private final DiscountPolicyId policyId;
    private final long orderId;
    private final long memberId;
    private final long productGroupId;
    private final int originalPrice;
    private final int discountAmount;
    private final int finalPrice;
    private final Instant usedAt;

    private DiscountUsage(
            DiscountUsageId id,
            DiscountPolicyId policyId,
            long orderId,
            long memberId,
            long productGroupId,
            int originalPrice,
            int discountAmount,
            int finalPrice,
            Instant usedAt) {
        this.id = id;
        this.policyId = policyId;
        this.orderId = orderId;
        this.memberId = memberId;
        this.productGroupId = productGroupId;
        this.originalPrice = originalPrice;
        this.discountAmount = discountAmount;
        this.finalPrice = finalPrice;
        this.usedAt = usedAt;
    }

    // ========== Factory Methods ==========

    /**
     * 새 할인 사용 기록 생성.
     *
     * @param policyId 적용된 할인 정책 ID
     * @param orderId 주문 ID
     * @param memberId 회원 ID
     * @param productGroupId 상품 그룹 ID
     * @param originalPrice 할인 전 가격
     * @param discountAmount 할인 금액
     * @param now 사용 시각
     * @return 새 DiscountUsage 인스턴스
     */
    public static DiscountUsage forNew(
            DiscountPolicyId policyId,
            long orderId,
            long memberId,
            long productGroupId,
            int originalPrice,
            int discountAmount,
            Instant now) {
        validatePrice(originalPrice, discountAmount);
        int finalPrice = originalPrice - discountAmount;
        return new DiscountUsage(
                DiscountUsageId.forNew(),
                policyId,
                orderId,
                memberId,
                productGroupId,
                originalPrice,
                discountAmount,
                finalPrice,
                now);
    }

    /**
     * 영속성 계층에서 복원.
     *
     * @return 복원된 DiscountUsage 인스턴스
     */
    public static DiscountUsage reconstitute(
            DiscountUsageId id,
            DiscountPolicyId policyId,
            long orderId,
            long memberId,
            long productGroupId,
            int originalPrice,
            int discountAmount,
            int finalPrice,
            Instant usedAt) {
        return new DiscountUsage(
                id,
                policyId,
                orderId,
                memberId,
                productGroupId,
                originalPrice,
                discountAmount,
                finalPrice,
                usedAt);
    }

    // ========== Validation ==========

    private static void validatePrice(int originalPrice, int discountAmount) {
        if (discountAmount <= 0) {
            throw new IllegalArgumentException("할인 금액은 0보다 커야 합니다");
        }
        if (originalPrice < discountAmount) {
            throw new IllegalArgumentException("원가보다 할인 금액이 클 수 없습니다");
        }
    }

    // ========== Condition Checks ==========

    /** 신규 생성 여부 확인 */
    public boolean isNew() {
        return id.isNew();
    }

    // ========== Accessor Methods ==========

    public DiscountUsageId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public DiscountPolicyId policyId() {
        return policyId;
    }

    public Long policyIdValue() {
        return policyId.value();
    }

    public long orderId() {
        return orderId;
    }

    public long memberId() {
        return memberId;
    }

    public long productGroupId() {
        return productGroupId;
    }

    public int originalPrice() {
        return originalPrice;
    }

    public int discountAmount() {
        return discountAmount;
    }

    public int finalPrice() {
        return finalPrice;
    }

    public Instant usedAt() {
        return usedAt;
    }
}
