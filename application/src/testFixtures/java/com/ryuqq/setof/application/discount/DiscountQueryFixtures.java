package com.ryuqq.setof.application.discount;

import com.ryuqq.setof.application.discount.dto.query.DiscountPolicySearchParams;
import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyPageResult;
import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyResult;
import com.ryuqq.setof.application.discount.dto.response.DiscountTargetResult;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * Discount Application Query 테스트 Fixtures.
 *
 * <p>할인 정책 조회 관련 Query/Result 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class DiscountQueryFixtures {

    private static final Instant FIXED_START = Instant.parse("2025-01-01T00:00:00Z");
    private static final Instant FIXED_END = Instant.parse("2025-12-31T23:59:59Z");
    private static final Instant FIXED_NOW = Instant.parse("2025-01-01T00:00:00Z");

    private DiscountQueryFixtures() {}

    // ===== DiscountPolicySearchParams =====

    public static DiscountPolicySearchParams defaultSearchParams() {
        return new DiscountPolicySearchParams(null, null, null, null, null, null, "DESC", 0, 20);
    }

    public static DiscountPolicySearchParams searchParams(int page, int size) {
        return new DiscountPolicySearchParams(
                null, null, null, null, null, null, "DESC", page, size);
    }

    public static DiscountPolicySearchParams searchParamsByApplicationType(String applicationType) {
        return new DiscountPolicySearchParams(
                applicationType, null, null, null, null, null, "DESC", 0, 20);
    }

    public static DiscountPolicySearchParams searchParamsByPublisherType(String publisherType) {
        return new DiscountPolicySearchParams(
                null, publisherType, null, null, null, null, "DESC", 0, 20);
    }

    public static DiscountPolicySearchParams searchParamsBySellerId(Long sellerId) {
        return new DiscountPolicySearchParams(
                null, null, null, sellerId, null, null, "DESC", 0, 20);
    }

    public static DiscountPolicySearchParams searchParamsActiveOnly() {
        return new DiscountPolicySearchParams(null, null, null, null, true, null, "DESC", 0, 20);
    }

    // ===== DiscountTargetResult =====

    public static DiscountTargetResult targetResult(Long id) {
        return new DiscountTargetResult(id, "PRODUCT", 300L);
    }

    public static List<DiscountTargetResult> targetResults() {
        return List.of(targetResult(1L), targetResult(2L));
    }

    // ===== DiscountPolicyResult =====

    public static DiscountPolicyResult discountPolicyResult(long id) {
        return new DiscountPolicyResult(
                id,
                "테스트할인정책",
                "테스트용 할인 정책입니다",
                "RATE",
                10.0,
                null,
                50000,
                true,
                10000,
                "INSTANT",
                "ADMIN",
                null,
                "PLATFORM_INSTANT",
                50,
                FIXED_START,
                FIXED_END,
                1000000,
                0,
                true,
                List.of(targetResult(1L)),
                FIXED_NOW,
                FIXED_NOW);
    }

    public static DiscountPolicyResult inactiveDiscountPolicyResult(long id) {
        return new DiscountPolicyResult(
                id,
                "비활성할인정책",
                "비활성 정책입니다",
                "FIXED_AMOUNT",
                null,
                5000,
                null,
                false,
                null,
                "INSTANT",
                "SELLER",
                1L,
                "SELLER_INSTANT",
                50,
                FIXED_START,
                FIXED_END,
                1000000,
                0,
                false,
                Collections.emptyList(),
                FIXED_NOW,
                FIXED_NOW);
    }

    public static List<DiscountPolicyResult> discountPolicyResults() {
        return List.of(discountPolicyResult(1L), discountPolicyResult(2L));
    }

    // ===== DiscountPolicyPageResult =====

    public static DiscountPolicyPageResult discountPolicyPageResult() {
        return DiscountPolicyPageResult.of(discountPolicyResults(), 2L, 0, 20);
    }

    public static DiscountPolicyPageResult emptyDiscountPolicyPageResult() {
        return DiscountPolicyPageResult.of(Collections.emptyList(), 0L, 0, 20);
    }
}
