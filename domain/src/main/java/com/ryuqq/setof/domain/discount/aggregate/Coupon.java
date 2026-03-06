package com.ryuqq.setof.domain.discount.aggregate;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.discount.id.CouponId;
import com.ryuqq.setof.domain.discount.id.DiscountPolicyId;
import com.ryuqq.setof.domain.discount.vo.CouponCode;
import com.ryuqq.setof.domain.discount.vo.CouponName;
import com.ryuqq.setof.domain.discount.vo.CouponType;
import com.ryuqq.setof.domain.discount.vo.DiscountPeriod;
import com.ryuqq.setof.domain.discount.vo.IssuanceLimit;
import java.time.Instant;

/**
 * Coupon - 쿠폰 템플릿 Aggregate Root.
 *
 * <p>쿠폰 발급 규칙과 수량을 관리합니다. 할인 규칙은 DiscountPolicy에 위임합니다.
 *
 * <p>주요 불변식:
 *
 * <ul>
 *   <li>CODE 타입 → couponCode 필수
 *   <li>issuedCount ≤ totalCount
 *   <li>discountPolicyId는 applicationType=COUPON인 정책만 참조
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public class Coupon {

    private final CouponId id;
    private final DiscountPolicyId discountPolicyId;
    private CouponName couponName;
    private String description;
    private final CouponType couponType;
    private CouponCode couponCode;
    private IssuanceLimit issuanceLimit;
    private int issuedCount;
    private DiscountPeriod usagePeriod;
    private boolean active;
    private DeletionStatus deletionStatus;
    private final Instant createdAt;
    private Instant updatedAt;

    private Coupon(
            CouponId id,
            DiscountPolicyId discountPolicyId,
            CouponName couponName,
            String description,
            CouponType couponType,
            CouponCode couponCode,
            IssuanceLimit issuanceLimit,
            int issuedCount,
            DiscountPeriod usagePeriod,
            boolean active,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.discountPolicyId = discountPolicyId;
        this.couponName = couponName;
        this.description = description;
        this.couponType = couponType;
        this.couponCode = couponCode;
        this.issuanceLimit = issuanceLimit;
        this.issuedCount = issuedCount;
        this.usagePeriod = usagePeriod;
        this.active = active;
        this.deletionStatus = deletionStatus != null ? deletionStatus : DeletionStatus.active();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ========== Factory Methods ==========

    /**
     * 새 쿠폰 생성.
     *
     * @param discountPolicyId 할인 규칙 참조 ID (필수)
     * @param couponName 쿠폰 표시명 (필수)
     * @param description 설명 (nullable)
     * @param couponType 쿠폰 발급 방식 (필수)
     * @param couponCode 쿠폰 코드 (CODE 타입 시 필수)
     * @param issuanceLimit 발급 제한 (필수)
     * @param usagePeriod 사용 가능 기간 (필수)
     * @param now 생성 시각
     * @return 새 Coupon 인스턴스
     */
    public static Coupon forNew(
            DiscountPolicyId discountPolicyId,
            CouponName couponName,
            String description,
            CouponType couponType,
            CouponCode couponCode,
            IssuanceLimit issuanceLimit,
            DiscountPeriod usagePeriod,
            Instant now) {
        validateCouponCode(couponType, couponCode);

        return new Coupon(
                CouponId.forNew(),
                discountPolicyId,
                couponName,
                description,
                couponType,
                couponCode,
                issuanceLimit,
                0,
                usagePeriod,
                true,
                DeletionStatus.active(),
                now,
                now);
    }

    /**
     * 영속성 계층에서 엔티티 복원.
     *
     * @return 복원된 Coupon 인스턴스
     */
    public static Coupon reconstitute(
            CouponId id,
            DiscountPolicyId discountPolicyId,
            CouponName couponName,
            String description,
            CouponType couponType,
            CouponCode couponCode,
            IssuanceLimit issuanceLimit,
            int issuedCount,
            DiscountPeriod usagePeriod,
            boolean active,
            Instant deletedAt,
            Instant createdAt,
            Instant updatedAt) {
        DeletionStatus status =
                deletedAt != null ? DeletionStatus.deletedAt(deletedAt) : DeletionStatus.active();
        return new Coupon(
                id,
                discountPolicyId,
                couponName,
                description,
                couponType,
                couponCode,
                issuanceLimit,
                issuedCount,
                usagePeriod,
                active,
                status,
                createdAt,
                updatedAt);
    }

    // ========== Validation ==========

    private static void validateCouponCode(CouponType couponType, CouponCode couponCode) {
        if (couponType == CouponType.CODE && couponCode == null) {
            throw new IllegalArgumentException("CODE 타입 쿠폰은 쿠폰 코드가 필수입니다");
        }
    }

    // ========== Business Methods ==========

    /** 신규 생성 여부 확인 */
    public boolean isNew() {
        return id.isNew();
    }

    /**
     * 쿠폰 정보 수정.
     *
     * @param data 수정 데이터
     * @param now 수정 시각
     */
    public void update(CouponUpdateData data, Instant now) {
        this.couponName = data.couponName();
        this.description = data.description();
        this.couponCode = data.couponCode();
        this.issuanceLimit = data.issuanceLimit();
        this.usagePeriod = data.usagePeriod();
        this.updatedAt = now;
    }

    /** 총 수량 기준 발급 가능 여부 */
    public boolean canIssue() {
        return issuanceLimit.canIssue(issuedCount);
    }

    /** 인당 수량 기준 발급 가능 여부 */
    public boolean canIssueToUser(int userIssuedCount) {
        return issuanceLimit.canIssueToUser(userIssuedCount);
    }

    /**
     * 발급 카운트 증가.
     *
     * @param now 발급 시각
     */
    public void incrementIssuedCount(Instant now) {
        this.issuedCount++;
        this.updatedAt = now;
    }

    /** CODE 타입 코드 매칭 검증 */
    public boolean matchesCode(String inputCode) {
        if (couponType != CouponType.CODE || couponCode == null) {
            return false;
        }
        return couponCode.matchesIgnoreCase(inputCode);
    }

    /**
     * 쿠폰 활성화.
     *
     * @param now 활성화 시각
     */
    public void activate(Instant now) {
        this.active = true;
        this.updatedAt = now;
    }

    /**
     * 쿠폰 비활성화.
     *
     * @param now 비활성화 시각
     */
    public void deactivate(Instant now) {
        this.active = false;
        this.updatedAt = now;
    }

    /**
     * 쿠폰 삭제 (Soft Delete).
     *
     * @param now 삭제 시각
     */
    public void delete(Instant now) {
        this.deletionStatus = DeletionStatus.deletedAt(now);
        this.active = false;
        this.updatedAt = now;
    }

    // ========== Accessor Methods ==========

    public CouponId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public DiscountPolicyId discountPolicyId() {
        return discountPolicyId;
    }

    public Long discountPolicyIdValue() {
        return discountPolicyId.value();
    }

    public CouponName couponName() {
        return couponName;
    }

    public String couponNameValue() {
        return couponName.value();
    }

    public String description() {
        return description;
    }

    public CouponType couponType() {
        return couponType;
    }

    public CouponCode couponCode() {
        return couponCode;
    }

    public String couponCodeValue() {
        return couponCode != null ? couponCode.value() : null;
    }

    public IssuanceLimit issuanceLimit() {
        return issuanceLimit;
    }

    public int totalCount() {
        return issuanceLimit.totalCount();
    }

    public int perUserCount() {
        return issuanceLimit.perUserCount();
    }

    public int issuedCount() {
        return issuedCount;
    }

    public DiscountPeriod usagePeriod() {
        return usagePeriod;
    }

    public boolean isActive() {
        return active;
    }

    public DeletionStatus deletionStatus() {
        return deletionStatus;
    }

    public boolean isDeleted() {
        return deletionStatus.isDeleted();
    }

    public Instant deletedAt() {
        return deletionStatus.deletedAt();
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
