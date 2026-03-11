package com.ryuqq.setof.application.discount;

import com.ryuqq.setof.domain.discount.DiscountFixtures;
import com.ryuqq.setof.domain.discount.aggregate.DiscountOutbox;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceRow;
import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceUpdateData;
import com.ryuqq.setof.domain.discount.id.DiscountOutboxId;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.OutboxStatus;
import com.ryuqq.setof.domain.discount.vo.OutboxTargetKey;
import com.ryuqq.setof.domain.discount.vo.StackingGroup;
import java.time.Instant;
import java.util.List;

/**
 * Discount Application 도메인 객체 테스트 Fixtures.
 *
 * <p>Application 레이어 테스트에서 사용하는 Discount 관련 도메인 객체 생성 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class DiscountDomainFixtures {

    private static final Instant FIXED_NOW = Instant.parse("2025-01-01T00:00:00Z");
    private static final Instant FIXED_YESTERDAY = FIXED_NOW.minusSeconds(86400);
    private static final Instant FIXED_TOMORROW = FIXED_NOW.plusSeconds(86400);

    public static final DiscountTargetType DEFAULT_TARGET_TYPE = DiscountTargetType.SELLER;
    public static final long DEFAULT_TARGET_ID = 1L;
    public static final long DEFAULT_OUTBOX_ID = 10L;
    public static final long DEFAULT_PRODUCT_GROUP_ID = 100L;
    public static final int DEFAULT_REGULAR_PRICE = 100000;
    public static final int DEFAULT_CURRENT_PRICE = 90000;

    private DiscountDomainFixtures() {}

    // ===== DiscountOutbox Fixtures =====

    /** PENDING 상태 아웃박스 */
    public static DiscountOutbox pendingOutbox() {
        return pendingOutbox(DEFAULT_OUTBOX_ID);
    }

    public static DiscountOutbox pendingOutbox(long id) {
        return DiscountOutbox.reconstitute(
                DiscountOutboxId.of(id),
                OutboxTargetKey.of(DEFAULT_TARGET_TYPE, DEFAULT_TARGET_ID),
                OutboxStatus.PENDING,
                0,
                null,
                null,
                FIXED_YESTERDAY,
                FIXED_YESTERDAY);
    }

    /** PUBLISHED 상태 아웃박스 */
    public static DiscountOutbox publishedOutbox() {
        return publishedOutbox(DEFAULT_OUTBOX_ID);
    }

    public static DiscountOutbox publishedOutbox(long id) {
        return DiscountOutbox.reconstitute(
                DiscountOutboxId.of(id),
                OutboxTargetKey.of(DEFAULT_TARGET_TYPE, DEFAULT_TARGET_ID),
                OutboxStatus.PUBLISHED,
                0,
                "SELLER:1",
                null,
                FIXED_YESTERDAY,
                FIXED_YESTERDAY);
    }

    /** PUBLISHED 상태 - stuck 상태 (오래된 updatedAt) */
    public static DiscountOutbox stuckPublishedOutbox(long id) {
        Instant longAgo = FIXED_NOW.minusSeconds(3600);
        return DiscountOutbox.reconstitute(
                DiscountOutboxId.of(id),
                OutboxTargetKey.of(DEFAULT_TARGET_TYPE, DEFAULT_TARGET_ID),
                OutboxStatus.PUBLISHED,
                1,
                "SELLER:1",
                null,
                FIXED_YESTERDAY,
                longAgo);
    }

    /** COMPLETED 상태 아웃박스 */
    public static DiscountOutbox completedOutbox() {
        return DiscountOutbox.reconstitute(
                DiscountOutboxId.of(DEFAULT_OUTBOX_ID),
                OutboxTargetKey.of(DEFAULT_TARGET_TYPE, DEFAULT_TARGET_ID),
                OutboxStatus.COMPLETED,
                0,
                "SELLER:1",
                null,
                FIXED_YESTERDAY,
                FIXED_YESTERDAY);
    }

    /** FAILED 상태 아웃박스 */
    public static DiscountOutbox failedOutbox() {
        return DiscountOutbox.reconstitute(
                DiscountOutboxId.of(DEFAULT_OUTBOX_ID),
                OutboxTargetKey.of(DEFAULT_TARGET_TYPE, DEFAULT_TARGET_ID),
                OutboxStatus.FAILED,
                3,
                null,
                "Recalculation failed: some error",
                FIXED_YESTERDAY,
                FIXED_YESTERDAY);
    }

    /** 재시도 2회인 아웃박스 (maxRetry 직전) */
    public static DiscountOutbox outboxWithRetryCount(int retryCount) {
        return DiscountOutbox.reconstitute(
                DiscountOutboxId.of(DEFAULT_OUTBOX_ID),
                OutboxTargetKey.of(DEFAULT_TARGET_TYPE, DEFAULT_TARGET_ID),
                OutboxStatus.PUBLISHED,
                retryCount,
                "SELLER:1",
                null,
                FIXED_YESTERDAY,
                FIXED_YESTERDAY);
    }

    // ===== ProductGroupPriceRow Fixtures =====

    public static ProductGroupPriceRow priceRow() {
        return priceRow(DEFAULT_PRODUCT_GROUP_ID, DEFAULT_REGULAR_PRICE, DEFAULT_CURRENT_PRICE);
    }

    public static ProductGroupPriceRow priceRow(
            long productGroupId, int regularPrice, int currentPrice) {
        return new ProductGroupPriceRow(productGroupId, regularPrice, currentPrice);
    }

    public static List<ProductGroupPriceRow> priceRows() {
        return List.of(
                priceRow(100L, 100000, 90000),
                priceRow(101L, 80000, 80000),
                priceRow(102L, 60000, 55000));
    }

    public static List<ProductGroupPriceRow> priceRows(int count) {
        return java.util.stream.LongStream.range(0, count)
                .mapToObj(i -> priceRow(100L + i, 100000, 90000))
                .toList();
    }

    // ===== ProductGroupPriceUpdateData Fixtures =====

    public static ProductGroupPriceUpdateData priceUpdateData() {
        return new ProductGroupPriceUpdateData(
                DEFAULT_PRODUCT_GROUP_ID, 85000, 15, 6, 5000, List.of());
    }

    public static ProductGroupPriceUpdateData priceUpdateData(long productGroupId) {
        return new ProductGroupPriceUpdateData(productGroupId, 85000, 15, 6, 5000, List.of());
    }

    public static List<ProductGroupPriceUpdateData> priceUpdateDataList() {
        return List.of(priceUpdateData(100L), priceUpdateData(101L), priceUpdateData(102L));
    }

    // ===== DiscountPolicy Fixtures (delegate to domain DiscountFixtures) =====

    /** 적용 가능한 할인 정책 목록 (FIXED_AMOUNT, SELLER_INSTANT) */
    public static List<DiscountPolicy> applicablePolicies() {
        return List.of(
                DiscountFixtures.fixedAmountPolicy(1L, 5000, StackingGroup.SELLER_INSTANT),
                DiscountFixtures.fixedAmountPolicy(2L, 3000, StackingGroup.PLATFORM_INSTANT));
    }

    /** 빈 할인 정책 목록 */
    public static List<DiscountPolicy> emptyPolicies() {
        return List.of();
    }

    // ===== 시간 상수 =====

    public static Instant fixedNow() {
        return FIXED_NOW;
    }

    public static Instant fixedYesterday() {
        return FIXED_YESTERDAY;
    }

    public static Instant fixedTomorrow() {
        return FIXED_TOMORROW;
    }
}
