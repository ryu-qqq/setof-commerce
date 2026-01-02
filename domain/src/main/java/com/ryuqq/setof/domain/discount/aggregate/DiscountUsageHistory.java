package com.ryuqq.setof.domain.discount.aggregate;

import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.discount.vo.CostShare;
import com.ryuqq.setof.domain.discount.vo.DiscountAmount;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyId;
import com.ryuqq.setof.domain.discount.vo.DiscountUsageHistoryId;
import com.ryuqq.setof.domain.order.vo.OrderId;
import com.ryuqq.setof.domain.order.vo.OrderMoney;
import java.time.Instant;
import java.util.Objects;

/**
 * DiscountUsageHistory Aggregate Root
 *
 * <p>할인 정책이 실제로 사용된 내역을 기록합니다.
 *
 * <p>비즈니스 규칙:
 *
 * <ul>
 *   <li>Checkout 완료 시점에만 생성됨 (결제 성공 후)
 *   <li>CostShare를 스냅샷으로 저장 - 정책 변경에 영향받지 않음
 *   <li>OrderId와 연결 - 주문 취소 시 사용 횟수 복원 가능
 *   <li>정책당 고객별/전체 사용 횟수 추적 가능
 * </ul>
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
public class DiscountUsageHistory {

    /** 히스토리 고유 식별자 (Auto Increment) */
    private final DiscountUsageHistoryId id;

    /** 사용된 할인 정책 ID */
    private final DiscountPolicyId discountPolicyId;

    /** 할인을 사용한 회원 ID (UUIDv7) */
    private final String memberId;

    /** 결제 세션 ID */
    private final CheckoutId checkoutId;

    /** 주문 ID */
    private final OrderId orderId;

    /** 실제 적용된 할인 금액 */
    private final DiscountAmount appliedAmount;

    /** 할인 전 원래 금액 */
    private final OrderMoney originalAmount;

    /** 비용 분담 비율 스냅샷 (정책 변경 영향 방지) */
    private final CostShare costShareSnapshot;

    /** 플랫폼 부담 금액 (계산된 값 저장) */
    private final long platformCost;

    /** 셀러 부담 금액 (계산된 값 저장) */
    private final long sellerCost;

    /** 할인 사용 시점 */
    private final Instant usedAt;

    /** 레코드 생성 시점 */
    private final Instant createdAt;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private DiscountUsageHistory(
            DiscountUsageHistoryId id,
            DiscountPolicyId discountPolicyId,
            String memberId,
            CheckoutId checkoutId,
            OrderId orderId,
            DiscountAmount appliedAmount,
            OrderMoney originalAmount,
            CostShare costShareSnapshot,
            long platformCost,
            long sellerCost,
            Instant usedAt,
            Instant createdAt) {
        this.id = id;
        this.discountPolicyId =
                Objects.requireNonNull(discountPolicyId, "discountPolicyId는 필수입니다.");
        this.memberId = Objects.requireNonNull(memberId, "memberId는 필수입니다.");
        this.checkoutId = Objects.requireNonNull(checkoutId, "checkoutId는 필수입니다.");
        this.orderId = Objects.requireNonNull(orderId, "orderId는 필수입니다.");
        this.appliedAmount = Objects.requireNonNull(appliedAmount, "appliedAmount는 필수입니다.");
        this.originalAmount = Objects.requireNonNull(originalAmount, "originalAmount는 필수입니다.");
        this.costShareSnapshot =
                Objects.requireNonNull(costShareSnapshot, "costShareSnapshot는 필수입니다.");
        this.platformCost = platformCost;
        this.sellerCost = sellerCost;
        this.usedAt = Objects.requireNonNull(usedAt, "usedAt는 필수입니다.");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt는 필수입니다.");
    }

    /**
     * Static Factory Method - 신규 히스토리 생성
     *
     * <p>Checkout 완료 시 호출되어 할인 사용 내역을 기록합니다.
     *
     * @param discountPolicyId 사용된 할인 정책 ID
     * @param memberId 회원 ID
     * @param checkoutId 결제 세션 ID
     * @param orderId 주문 ID
     * @param appliedAmount 실제 적용된 할인 금액
     * @param originalAmount 할인 전 원래 금액
     * @param costShare 비용 분담 비율 (스냅샷으로 저장)
     * @param now 현재 시간
     * @return 새로운 DiscountUsageHistory 인스턴스
     */
    public static DiscountUsageHistory forNew(
            DiscountPolicyId discountPolicyId,
            String memberId,
            CheckoutId checkoutId,
            OrderId orderId,
            DiscountAmount appliedAmount,
            OrderMoney originalAmount,
            CostShare costShare,
            Instant now) {
        long discountValue = appliedAmount.value();
        long platformCostValue = costShare.calculatePlatformCost(discountValue);
        long sellerCostValue = costShare.calculateSellerCost(discountValue);

        return new DiscountUsageHistory(
                null, // 신규 생성 시 ID는 null (Persistence Layer에서 할당)
                discountPolicyId,
                memberId,
                checkoutId,
                orderId,
                appliedAmount,
                originalAmount,
                costShare,
                platformCostValue,
                sellerCostValue,
                now,
                now);
    }

    /**
     * Static Factory Method - 기존 데이터 복원
     *
     * <p>Persistence Layer에서 조회된 데이터를 도메인 객체로 복원합니다.
     *
     * @param id 히스토리 ID
     * @param discountPolicyId 할인 정책 ID
     * @param memberId 회원 ID
     * @param checkoutId 결제 세션 ID
     * @param orderId 주문 ID
     * @param appliedAmount 적용된 할인 금액
     * @param originalAmount 원래 금액
     * @param costShareSnapshot 비용 분담 스냅샷
     * @param platformCost 플랫폼 부담액
     * @param sellerCost 셀러 부담액
     * @param usedAt 사용 시점
     * @param createdAt 생성 시점
     * @return DiscountUsageHistory 인스턴스
     */
    public static DiscountUsageHistory fromPersistence(
            DiscountUsageHistoryId id,
            DiscountPolicyId discountPolicyId,
            String memberId,
            CheckoutId checkoutId,
            OrderId orderId,
            DiscountAmount appliedAmount,
            OrderMoney originalAmount,
            CostShare costShareSnapshot,
            long platformCost,
            long sellerCost,
            Instant usedAt,
            Instant createdAt) {
        return new DiscountUsageHistory(
                id,
                discountPolicyId,
                memberId,
                checkoutId,
                orderId,
                appliedAmount,
                originalAmount,
                costShareSnapshot,
                platformCost,
                sellerCost,
                usedAt,
                createdAt);
    }

    // === Identity Accessors (Law of Demeter 준수) ===

    /**
     * 히스토리 ID 값 반환
     *
     * @return ID 값 (신규 생성 시 null 가능)
     */
    public Long idValue() {
        return id != null ? id.value() : null;
    }

    /**
     * 할인 정책 ID 값 반환
     *
     * @return 정책 ID 값
     */
    public Long discountPolicyIdValue() {
        return discountPolicyId.value();
    }

    /**
     * 회원 ID 반환
     *
     * @return 회원 ID
     */
    public String memberId() {
        return memberId;
    }

    /**
     * 결제 세션 ID 값 반환
     *
     * @return Checkout UUID
     */
    public String checkoutIdValue() {
        return checkoutId.value().toString();
    }

    /**
     * 주문 ID 값 반환
     *
     * @return Order UUID
     */
    public String orderIdValue() {
        return orderId.value().toString();
    }

    // === Amount Accessors ===

    /**
     * 적용된 할인 금액 반환
     *
     * @return 할인 금액 (원)
     */
    public long appliedAmountValue() {
        return appliedAmount.value();
    }

    /**
     * 원래 금액 반환
     *
     * @return 할인 전 금액 (원)
     */
    public long originalAmountValue() {
        return originalAmount.toLong();
    }

    /**
     * 플랫폼 부담 금액 반환
     *
     * @return 플랫폼 부담액 (원)
     */
    public long platformCost() {
        return platformCost;
    }

    /**
     * 셀러 부담 금액 반환
     *
     * @return 셀러 부담액 (원)
     */
    public long sellerCost() {
        return sellerCost;
    }

    // === CostShare Snapshot Accessors ===

    /**
     * 플랫폼 분담 비율 반환
     *
     * @return 플랫폼 분담 비율
     */
    public java.math.BigDecimal platformRatio() {
        return costShareSnapshot.platformRatio();
    }

    /**
     * 셀러 분담 비율 반환
     *
     * @return 셀러 분담 비율
     */
    public java.math.BigDecimal sellerRatio() {
        return costShareSnapshot.sellerRatio();
    }

    // === Timestamp Accessors ===

    /**
     * 사용 시점 반환
     *
     * @return 할인 사용 시점
     */
    public Instant usedAt() {
        return usedAt;
    }

    /**
     * 생성 시점 반환
     *
     * @return 레코드 생성 시점
     */
    public Instant createdAt() {
        return createdAt;
    }

    // === Business Methods ===

    /**
     * 할인 적용률 계산
     *
     * @return 할인율 (0.0 ~ 100.0%)
     */
    public double discountPercentage() {
        if (originalAmountValue() == 0) {
            return 0.0;
        }
        return (appliedAmountValue() * 100.0) / originalAmountValue();
    }

    /**
     * 0원 할인인지 확인
     *
     * @return 할인 금액이 0원이면 true
     */
    public boolean isZeroDiscount() {
        return appliedAmount.isZero();
    }

    // === Object Methods ===

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountUsageHistory that = (DiscountUsageHistory) o;
        if (id == null || that.id == null) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return String.format(
                "DiscountUsageHistory{id=%s, policyId=%s, memberId='%s', orderId='%s', amount=%d}",
                idValue(), discountPolicyIdValue(), memberId, orderIdValue(), appliedAmountValue());
    }
}
