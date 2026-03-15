package com.ryuqq.setof.domain.discount;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.discount.aggregate.Coupon;
import com.ryuqq.setof.domain.discount.aggregate.CouponUpdateData;
import com.ryuqq.setof.domain.discount.aggregate.DiscountOutbox;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicyUpdateData;
import com.ryuqq.setof.domain.discount.aggregate.DiscountTarget;
import com.ryuqq.setof.domain.discount.aggregate.IssuedCoupon;
import com.ryuqq.setof.domain.discount.id.CouponId;
import com.ryuqq.setof.domain.discount.id.DiscountOutboxId;
import com.ryuqq.setof.domain.discount.id.DiscountPolicyId;
import com.ryuqq.setof.domain.discount.id.DiscountTargetId;
import com.ryuqq.setof.domain.discount.id.IssuedCouponId;
import com.ryuqq.setof.domain.discount.vo.ApplicationType;
import com.ryuqq.setof.domain.discount.vo.CouponCode;
import com.ryuqq.setof.domain.discount.vo.CouponName;
import com.ryuqq.setof.domain.discount.vo.CouponStatus;
import com.ryuqq.setof.domain.discount.vo.CouponType;
import com.ryuqq.setof.domain.discount.vo.DiscountBudget;
import com.ryuqq.setof.domain.discount.vo.DiscountMethod;
import com.ryuqq.setof.domain.discount.vo.DiscountPeriod;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyName;
import com.ryuqq.setof.domain.discount.vo.DiscountRate;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.IssuanceLimit;
import com.ryuqq.setof.domain.discount.vo.OutboxStatus;
import com.ryuqq.setof.domain.discount.vo.OutboxTargetKey;
import com.ryuqq.setof.domain.discount.vo.Priority;
import com.ryuqq.setof.domain.discount.vo.PublisherType;
import com.ryuqq.setof.domain.discount.vo.StackingGroup;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.time.Instant;
import java.util.List;

/**
 * Discount 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 Discount 관련 객체들을 생성합니다.
 */
public final class DiscountFixtures {

    private DiscountFixtures() {}

    // ===== 상수 =====
    public static final String DEFAULT_POLICY_NAME = "테스트할인정책";
    public static final String DEFAULT_DESCRIPTION = "테스트용 할인 정책입니다";
    public static final double DEFAULT_DISCOUNT_RATE = 10.0;
    public static final int DEFAULT_FIXED_AMOUNT = 5000;
    public static final int DEFAULT_MAX_DISCOUNT_AMOUNT = 50000;
    public static final int DEFAULT_MINIMUM_ORDER_AMOUNT = 10000;
    public static final int DEFAULT_PRIORITY = 50;
    public static final int DEFAULT_TOTAL_BUDGET = 1000000;
    public static final int DEFAULT_TOTAL_COUNT = 1000;
    public static final int DEFAULT_PER_USER_COUNT = 1;
    public static final String DEFAULT_COUPON_NAME = "테스트쿠폰";
    public static final String DEFAULT_COUPON_CODE = "TEST1234";
    public static final long DEFAULT_USER_ID = 100L;
    public static final long DEFAULT_ORDER_ID = 200L;
    public static final long DEFAULT_TARGET_ID = 300L;

    // ===== DiscountPolicy Fixtures =====

    /** 신규 RATE 즉시할인 정책 */
    public static DiscountPolicy newRateInstantPolicy() {
        return DiscountPolicy.forNew(
                defaultPolicyName(),
                DEFAULT_DESCRIPTION,
                DiscountMethod.RATE,
                defaultDiscountRate(),
                null,
                Money.of(DEFAULT_MAX_DISCOUNT_AMOUNT),
                true,
                Money.of(DEFAULT_MINIMUM_ORDER_AMOUNT),
                ApplicationType.INSTANT,
                PublisherType.ADMIN,
                null,
                StackingGroup.PLATFORM_INSTANT,
                defaultPriority(),
                defaultActivePeriod(),
                defaultBudget(),
                CommonVoFixtures.now());
    }

    /** 신규 FIXED_AMOUNT 즉시할인 정책 */
    public static DiscountPolicy newFixedInstantPolicy() {
        return DiscountPolicy.forNew(
                defaultPolicyName(),
                DEFAULT_DESCRIPTION,
                DiscountMethod.FIXED_AMOUNT,
                null,
                Money.of(DEFAULT_FIXED_AMOUNT),
                null,
                false,
                null,
                ApplicationType.INSTANT,
                PublisherType.SELLER,
                SellerId.of(1L),
                StackingGroup.SELLER_INSTANT,
                defaultPriority(),
                defaultActivePeriod(),
                defaultBudget(),
                CommonVoFixtures.now());
    }

    /** 신규 쿠폰 타입 할인 정책 */
    public static DiscountPolicy newCouponPolicy() {
        return DiscountPolicy.forNew(
                DiscountPolicyName.of("쿠폰할인정책"),
                DEFAULT_DESCRIPTION,
                DiscountMethod.RATE,
                DiscountRate.of(20.0),
                null,
                Money.of(30000),
                true,
                null,
                ApplicationType.COUPON,
                PublisherType.ADMIN,
                null,
                StackingGroup.COUPON,
                Priority.of(30),
                defaultActivePeriod(),
                defaultBudget(),
                CommonVoFixtures.now());
    }

    /** 영속성 복원 - 활성 RATE 정책 */
    public static DiscountPolicy activeRatePolicy() {
        return activeRatePolicy(1L);
    }

    /** 영속성 복원 - 활성 RATE 정책 (ID 지정) */
    public static DiscountPolicy activeRatePolicy(Long id) {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(id),
                defaultPolicyName(),
                DEFAULT_DESCRIPTION,
                DiscountMethod.RATE,
                defaultDiscountRate(),
                null,
                Money.of(DEFAULT_MAX_DISCOUNT_AMOUNT),
                true,
                Money.of(DEFAULT_MINIMUM_ORDER_AMOUNT),
                ApplicationType.INSTANT,
                PublisherType.ADMIN,
                null,
                StackingGroup.PLATFORM_INSTANT,
                defaultPriority(),
                defaultActivePeriod(),
                defaultBudget(),
                true,
                null,
                List.of(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 영속성 복원 - 활성 FIXED 정책 */
    public static DiscountPolicy activeFixedPolicy(Long id) {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(id),
                defaultPolicyName(),
                DEFAULT_DESCRIPTION,
                DiscountMethod.FIXED_AMOUNT,
                null,
                Money.of(DEFAULT_FIXED_AMOUNT),
                null,
                false,
                null,
                ApplicationType.INSTANT,
                PublisherType.SELLER,
                SellerId.of(1L),
                StackingGroup.SELLER_INSTANT,
                defaultPriority(),
                defaultActivePeriod(),
                defaultBudget(),
                true,
                null,
                List.of(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 영속성 복원 - 비활성 정책 */
    public static DiscountPolicy inactivePolicy() {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(2L),
                defaultPolicyName(),
                DEFAULT_DESCRIPTION,
                DiscountMethod.RATE,
                defaultDiscountRate(),
                null,
                null,
                false,
                null,
                ApplicationType.INSTANT,
                PublisherType.ADMIN,
                null,
                StackingGroup.PLATFORM_INSTANT,
                defaultPriority(),
                defaultActivePeriod(),
                defaultBudget(),
                false,
                null,
                List.of(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 영속성 복원 - 삭제된 정책 */
    public static DiscountPolicy deletedPolicy() {
        Instant deletedAt = CommonVoFixtures.yesterday();
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(3L),
                defaultPolicyName(),
                DEFAULT_DESCRIPTION,
                DiscountMethod.RATE,
                defaultDiscountRate(),
                null,
                null,
                false,
                null,
                ApplicationType.INSTANT,
                PublisherType.ADMIN,
                null,
                StackingGroup.PLATFORM_INSTANT,
                defaultPriority(),
                defaultActivePeriod(),
                defaultBudget(),
                false,
                deletedAt,
                List.of(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 특정 우선순위의 정책 생성 (스태킹 테스트용) */
    public static DiscountPolicy activePolicyWithPriority(
            Long id, int priority, StackingGroup group) {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(id),
                defaultPolicyName(),
                DEFAULT_DESCRIPTION,
                DiscountMethod.RATE,
                DiscountRate.of(10.0),
                null,
                null,
                false,
                null,
                ApplicationType.INSTANT,
                PublisherType.ADMIN,
                null,
                group,
                Priority.of(priority),
                defaultActivePeriod(),
                defaultBudget(),
                true,
                null,
                List.of(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** FIXED 금액 지정 정책 (계산 테스트용) */
    public static DiscountPolicy fixedAmountPolicy(Long id, int amount, StackingGroup group) {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(id),
                defaultPolicyName(),
                DEFAULT_DESCRIPTION,
                DiscountMethod.FIXED_AMOUNT,
                null,
                Money.of(amount),
                null,
                false,
                null,
                ApplicationType.INSTANT,
                PublisherType.ADMIN,
                null,
                group,
                defaultPriority(),
                defaultActivePeriod(),
                defaultBudget(),
                true,
                null,
                List.of(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 예산 소진된 정책 */
    public static DiscountPolicy exhaustedBudgetPolicy() {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(4L),
                defaultPolicyName(),
                DEFAULT_DESCRIPTION,
                DiscountMethod.RATE,
                defaultDiscountRate(),
                null,
                null,
                false,
                null,
                ApplicationType.INSTANT,
                PublisherType.ADMIN,
                null,
                StackingGroup.PLATFORM_INSTANT,
                defaultPriority(),
                defaultActivePeriod(),
                DiscountBudget.of(Money.of(DEFAULT_TOTAL_BUDGET), Money.of(DEFAULT_TOTAL_BUDGET)),
                true,
                null,
                List.of(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    // ===== DiscountTarget Fixtures =====

    public static DiscountTarget newTarget() {
        return DiscountTarget.forNew(DiscountTargetType.PRODUCT, DEFAULT_TARGET_ID);
    }

    public static DiscountTarget activeTarget(Long id) {
        return DiscountTarget.reconstitute(
                DiscountTargetId.of(id), DiscountTargetType.PRODUCT, DEFAULT_TARGET_ID, true);
    }

    public static DiscountTarget inactiveTarget(Long id) {
        return DiscountTarget.reconstitute(
                DiscountTargetId.of(id), DiscountTargetType.PRODUCT, DEFAULT_TARGET_ID, false);
    }

    // ===== Coupon Fixtures =====

    /** 신규 다운로드 쿠폰 */
    public static Coupon newDownloadCoupon() {
        return Coupon.forNew(
                DiscountPolicyId.of(10L),
                defaultCouponName(),
                DEFAULT_DESCRIPTION,
                CouponType.DOWNLOAD,
                null,
                defaultIssuanceLimit(),
                defaultActivePeriod(),
                CommonVoFixtures.now());
    }

    /** 신규 코드 쿠폰 */
    public static Coupon newCodeCoupon() {
        return Coupon.forNew(
                DiscountPolicyId.of(10L),
                defaultCouponName(),
                DEFAULT_DESCRIPTION,
                CouponType.CODE,
                defaultCouponCode(),
                defaultIssuanceLimit(),
                defaultActivePeriod(),
                CommonVoFixtures.now());
    }

    /** 영속성 복원 - 활성 다운로드 쿠폰 */
    public static Coupon activeDownloadCoupon() {
        return activeDownloadCoupon(1L);
    }

    public static Coupon activeDownloadCoupon(Long id) {
        return Coupon.reconstitute(
                CouponId.of(id),
                DiscountPolicyId.of(10L),
                defaultCouponName(),
                DEFAULT_DESCRIPTION,
                CouponType.DOWNLOAD,
                null,
                defaultIssuanceLimit(),
                0,
                defaultActivePeriod(),
                true,
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 영속성 복원 - 활성 코드 쿠폰 */
    public static Coupon activeCodeCoupon() {
        return Coupon.reconstitute(
                CouponId.of(2L),
                DiscountPolicyId.of(10L),
                defaultCouponName(),
                DEFAULT_DESCRIPTION,
                CouponType.CODE,
                defaultCouponCode(),
                defaultIssuanceLimit(),
                0,
                defaultActivePeriod(),
                true,
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 발급 수량 가득 찬 쿠폰 */
    public static Coupon fullIssuedCoupon() {
        return Coupon.reconstitute(
                CouponId.of(3L),
                DiscountPolicyId.of(10L),
                defaultCouponName(),
                DEFAULT_DESCRIPTION,
                CouponType.DOWNLOAD,
                null,
                defaultIssuanceLimit(),
                DEFAULT_TOTAL_COUNT,
                defaultActivePeriod(),
                true,
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    // ===== IssuedCoupon Fixtures =====

    /** 발급된 쿠폰 (ISSUED 상태) */
    public static IssuedCoupon issuedCoupon() {
        return IssuedCoupon.issue(
                CouponId.of(1L),
                DiscountPolicyId.of(10L),
                DEFAULT_USER_ID,
                CommonVoFixtures.tomorrow(),
                CommonVoFixtures.now());
    }

    /** 영속성 복원 - ISSUED 상태 */
    public static IssuedCoupon activeIssuedCoupon() {
        return activeIssuedCoupon(1L);
    }

    public static IssuedCoupon activeIssuedCoupon(Long id) {
        return IssuedCoupon.reconstitute(
                IssuedCouponId.of(id),
                CouponId.of(1L),
                DiscountPolicyId.of(10L),
                DEFAULT_USER_ID,
                CouponStatus.ISSUED,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.tomorrow(),
                null,
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 영속성 복원 - USED 상태 */
    public static IssuedCoupon usedIssuedCoupon() {
        return IssuedCoupon.reconstitute(
                IssuedCouponId.of(2L),
                CouponId.of(1L),
                DiscountPolicyId.of(10L),
                DEFAULT_USER_ID,
                CouponStatus.USED,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.tomorrow(),
                CommonVoFixtures.now(),
                DEFAULT_ORDER_ID,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 영속성 복원 - EXPIRED 상태 */
    public static IssuedCoupon expiredIssuedCoupon() {
        return IssuedCoupon.reconstitute(
                IssuedCouponId.of(3L),
                CouponId.of(1L),
                DiscountPolicyId.of(10L),
                DEFAULT_USER_ID,
                CouponStatus.EXPIRED,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday(),
                null,
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 영속성 복원 - CANCELLED 상태 */
    public static IssuedCoupon cancelledIssuedCoupon() {
        return IssuedCoupon.reconstitute(
                IssuedCouponId.of(4L),
                CouponId.of(1L),
                DiscountPolicyId.of(10L),
                DEFAULT_USER_ID,
                CouponStatus.CANCELLED,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.tomorrow(),
                null,
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    // ===== VO Fixtures =====

    public static DiscountPolicyName defaultPolicyName() {
        return DiscountPolicyName.of(DEFAULT_POLICY_NAME);
    }

    public static DiscountRate defaultDiscountRate() {
        return DiscountRate.of(DEFAULT_DISCOUNT_RATE);
    }

    public static Priority defaultPriority() {
        return Priority.of(DEFAULT_PRIORITY);
    }

    public static DiscountPeriod defaultActivePeriod() {
        return DiscountPeriod.of(CommonVoFixtures.yesterday(), CommonVoFixtures.tomorrow());
    }

    public static DiscountPeriod expiredPeriod() {
        Instant twoDaysAgo = Instant.now().minusSeconds(172800);
        return DiscountPeriod.of(twoDaysAgo, CommonVoFixtures.yesterday());
    }

    public static DiscountPeriod futurePeriod() {
        Instant twoDaysLater = Instant.now().plusSeconds(172800);
        return DiscountPeriod.of(CommonVoFixtures.tomorrow(), twoDaysLater);
    }

    public static DiscountBudget defaultBudget() {
        return DiscountBudget.of(Money.of(DEFAULT_TOTAL_BUDGET));
    }

    public static CouponName defaultCouponName() {
        return CouponName.of(DEFAULT_COUPON_NAME);
    }

    public static CouponCode defaultCouponCode() {
        return CouponCode.of(DEFAULT_COUPON_CODE);
    }

    public static IssuanceLimit defaultIssuanceLimit() {
        return IssuanceLimit.of(DEFAULT_TOTAL_COUNT, DEFAULT_PER_USER_COUNT);
    }

    // ===== DiscountOutbox Fixtures =====

    /** 신규 BRAND 타입 아웃박스 (PENDING 상태) */
    public static DiscountOutbox newBrandOutbox() {
        return DiscountOutbox.forNew(DiscountTargetType.BRAND, 100L, CommonVoFixtures.now());
    }

    /** 신규 SELLER 타입 아웃박스 (PENDING 상태) */
    public static DiscountOutbox newSellerOutbox() {
        return DiscountOutbox.forNew(DiscountTargetType.SELLER, 50L, CommonVoFixtures.now());
    }

    /** 영속성 복원 - PENDING 상태 아웃박스 */
    public static DiscountOutbox pendingOutbox() {
        return pendingOutbox(1L);
    }

    public static DiscountOutbox pendingOutbox(Long id) {
        return DiscountOutbox.reconstitute(
                DiscountOutboxId.of(id),
                OutboxTargetKey.of(DiscountTargetType.BRAND, 100L),
                OutboxStatus.PENDING,
                0,
                null,
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 영속성 복원 - PUBLISHED 상태 아웃박스 */
    public static DiscountOutbox publishedOutbox() {
        return DiscountOutbox.reconstitute(
                DiscountOutboxId.of(2L),
                OutboxTargetKey.of(DiscountTargetType.BRAND, 100L),
                OutboxStatus.PUBLISHED,
                0,
                "{\"targetType\":\"BRAND\",\"targetId\":100}",
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 영속성 복원 - COMPLETED 상태 아웃박스 */
    public static DiscountOutbox completedOutbox() {
        return DiscountOutbox.reconstitute(
                DiscountOutboxId.of(3L),
                OutboxTargetKey.of(DiscountTargetType.SELLER, 50L),
                OutboxStatus.COMPLETED,
                0,
                "{\"targetType\":\"SELLER\",\"targetId\":50}",
                null,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 영속성 복원 - FAILED 상태 아웃박스 (최대 재시도 소진) */
    public static DiscountOutbox failedOutbox() {
        return DiscountOutbox.reconstitute(
                DiscountOutboxId.of(4L),
                OutboxTargetKey.of(DiscountTargetType.CATEGORY, 200L),
                OutboxStatus.FAILED,
                3,
                null,
                "처리 실패",
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    // ===== UpdateData Fixtures =====

    public static DiscountPolicyUpdateData rateUpdateData() {
        return DiscountPolicyUpdateData.of(
                DiscountPolicyName.of("수정된정책명"),
                "수정된 설명",
                DiscountMethod.RATE,
                DiscountRate.of(15.0),
                null,
                Money.of(30000),
                true,
                Money.of(20000),
                Priority.of(70),
                defaultActivePeriod(),
                defaultBudget());
    }

    public static CouponUpdateData couponUpdateData() {
        return CouponUpdateData.of(
                CouponName.of("수정된쿠폰명"),
                "수정된 쿠폰 설명",
                null,
                IssuanceLimit.of(2000, 2),
                defaultActivePeriod());
    }
}
