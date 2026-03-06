package com.ryuqq.setof.domain.discount.aggregate;

import com.ryuqq.setof.domain.discount.id.CouponId;
import com.ryuqq.setof.domain.discount.id.DiscountPolicyId;
import com.ryuqq.setof.domain.discount.id.IssuedCouponId;
import com.ryuqq.setof.domain.discount.vo.CouponStatus;
import java.time.Instant;

/**
 * IssuedCoupon - 발급된 쿠폰 Aggregate Root.
 *
 * <p>개별 유저에게 발급된 쿠폰 인스턴스입니다. 사용/만료 상태를 추적합니다.
 *
 * <p>주요 불변식:
 *
 * <ul>
 *   <li>USED 상태 → orderId 필수, usedAt 필수
 *   <li>상태 전이: ISSUED → {USED, EXPIRED, CANCELLED}, USED → ISSUED (주문 취소 시)
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public class IssuedCoupon {

    private final IssuedCouponId id;
    private final CouponId couponId;
    private final DiscountPolicyId discountPolicyId;
    private final long userId;
    private CouponStatus status;
    private final Instant issuedAt;
    private final Instant expireAt;
    private Instant usedAt;
    private Long orderId;
    private final Instant createdAt;
    private Instant updatedAt;

    private IssuedCoupon(
            IssuedCouponId id,
            CouponId couponId,
            DiscountPolicyId discountPolicyId,
            long userId,
            CouponStatus status,
            Instant issuedAt,
            Instant expireAt,
            Instant usedAt,
            Long orderId,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.couponId = couponId;
        this.discountPolicyId = discountPolicyId;
        this.userId = userId;
        this.status = status;
        this.issuedAt = issuedAt;
        this.expireAt = expireAt;
        this.usedAt = usedAt;
        this.orderId = orderId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ========== Factory Methods ==========

    /**
     * 쿠폰 발급.
     *
     * @param couponId 쿠폰 템플릿 ID (필수)
     * @param discountPolicyId 할인 정책 ID (필수, 비정규화)
     * @param userId 유저 ID
     * @param expireAt 만료 시점
     * @param now 발급 시각
     * @return 새 IssuedCoupon 인스턴스 (ISSUED 상태)
     */
    public static IssuedCoupon issue(
            CouponId couponId,
            DiscountPolicyId discountPolicyId,
            long userId,
            Instant expireAt,
            Instant now) {
        return new IssuedCoupon(
                IssuedCouponId.forNew(),
                couponId,
                discountPolicyId,
                userId,
                CouponStatus.ISSUED,
                now,
                expireAt,
                null,
                null,
                now,
                now);
    }

    /**
     * 영속성 계층에서 엔티티 복원.
     *
     * @return 복원된 IssuedCoupon 인스턴스
     */
    public static IssuedCoupon reconstitute(
            IssuedCouponId id,
            CouponId couponId,
            DiscountPolicyId discountPolicyId,
            long userId,
            CouponStatus status,
            Instant issuedAt,
            Instant expireAt,
            Instant usedAt,
            Long orderId,
            Instant createdAt,
            Instant updatedAt) {
        return new IssuedCoupon(
                id,
                couponId,
                discountPolicyId,
                userId,
                status,
                issuedAt,
                expireAt,
                usedAt,
                orderId,
                createdAt,
                updatedAt);
    }

    // ========== Business Methods ==========

    /** 신규 생성 여부 확인 */
    public boolean isNew() {
        return id.isNew();
    }

    /**
     * 쿠폰 사용 (ISSUED → USED).
     *
     * @param orderId 사용된 주문 ID
     * @param now 사용 시각
     * @throws IllegalStateException 현재 상태에서 사용할 수 없는 경우
     */
    public void use(long orderId, Instant now) {
        if (status != CouponStatus.ISSUED) {
            throw new IllegalStateException(String.format("상태 %s에서는 쿠폰을 사용할 수 없습니다", status));
        }
        if (isExpired(now)) {
            throw new IllegalStateException("만료된 쿠폰은 사용할 수 없습니다");
        }
        this.status = CouponStatus.USED;
        this.orderId = orderId;
        this.usedAt = now;
        this.updatedAt = now;
    }

    /**
     * 쿠폰 만료 (ISSUED → EXPIRED).
     *
     * @param now 만료 처리 시각
     * @throws IllegalStateException 현재 상태에서 만료 처리할 수 없는 경우
     */
    public void expire(Instant now) {
        if (status != CouponStatus.ISSUED) {
            throw new IllegalStateException(String.format("상태 %s에서는 만료 처리할 수 없습니다", status));
        }
        this.status = CouponStatus.EXPIRED;
        this.updatedAt = now;
    }

    /**
     * 쿠폰 취소 (ISSUED → CANCELLED).
     *
     * @param now 취소 시각
     * @throws IllegalStateException 현재 상태에서 취소할 수 없는 경우
     */
    public void cancel(Instant now) {
        if (status != CouponStatus.ISSUED) {
            throw new IllegalStateException(String.format("상태 %s에서는 취소할 수 없습니다", status));
        }
        this.status = CouponStatus.CANCELLED;
        this.updatedAt = now;
    }

    /**
     * 쿠폰 사용 취소 (USED → ISSUED, 주문 취소 시).
     *
     * @param now 사용 취소 시각
     * @throws IllegalStateException 현재 상태가 USED가 아닌 경우
     */
    public void cancelUsage(Instant now) {
        if (status != CouponStatus.USED) {
            throw new IllegalStateException(String.format("상태 %s에서는 사용 취소를 할 수 없습니다", status));
        }
        this.status = CouponStatus.ISSUED;
        this.orderId = null;
        this.usedAt = null;
        this.updatedAt = now;
    }

    /** 사용 가능 여부 확인 (ISSUED 상태 + 미만료) */
    public boolean isUsable(Instant now) {
        return status == CouponStatus.ISSUED && !isExpired(now);
    }

    /** 만료 여부 확인 */
    public boolean isExpired(Instant now) {
        return !now.isBefore(expireAt);
    }

    // ========== Accessor Methods ==========

    public IssuedCouponId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public CouponId couponId() {
        return couponId;
    }

    public Long couponIdValue() {
        return couponId.value();
    }

    public DiscountPolicyId discountPolicyId() {
        return discountPolicyId;
    }

    public Long discountPolicyIdValue() {
        return discountPolicyId.value();
    }

    public long userId() {
        return userId;
    }

    public CouponStatus status() {
        return status;
    }

    public Instant issuedAt() {
        return issuedAt;
    }

    public Instant expireAt() {
        return expireAt;
    }

    public Instant usedAt() {
        return usedAt;
    }

    public Long orderId() {
        return orderId;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
