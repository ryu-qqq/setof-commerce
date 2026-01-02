package com.ryuqq.setof.integration.test.discount.fixture;

import com.ryuqq.setof.adapter.in.rest.admin.v2.discount.dto.command.RegisterDiscountPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.discount.dto.command.UpdateDiscountPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.discount.dto.command.UpdateDiscountTargetsV2ApiRequest;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Discount Integration Test Fixture
 *
 * <p>할인 정책 통합 테스트에서 사용하는 상수 및 Request Builder를 정의합니다.
 *
 * <p>테스트 데이터는 discount-test-data.sql과 일치해야 합니다.
 *
 * @since 1.0.0
 */
public final class DiscountIntegrationTestFixture {

    private DiscountIntegrationTestFixture() {
        // Utility class
    }

    // ============================================================
    // Test Data Constants (discount-test-data.sql과 일치)
    // ============================================================

    /** 테스트용 셀러 ID */
    public static final Long SELLER_ID = 1L;

    /** 다른 셀러 ID (권한 테스트용) */
    public static final Long OTHER_SELLER_ID = 2L;

    /** 정률 할인 정책 - 활성 상태 */
    public static final Long RATE_DISCOUNT_ACTIVE_ID = 1L;

    public static final String RATE_DISCOUNT_ACTIVE_NAME = "여름 시즌 10% 할인";
    public static final String DISCOUNT_GROUP_PRODUCT = "PRODUCT";
    public static final String DISCOUNT_TYPE_RATE = "RATE";
    public static final String TARGET_TYPE_ALL = "ALL";
    public static final int RATE_DISCOUNT_RATE = 10;
    public static final long RATE_DISCOUNT_MAX_AMOUNT = 10000L;
    public static final long RATE_DISCOUNT_MIN_ORDER = 30000L;

    /** 정액 할인 정책 - 활성 상태 */
    public static final Long FIXED_DISCOUNT_ACTIVE_ID = 2L;

    public static final String FIXED_DISCOUNT_ACTIVE_NAME = "5000원 즉시 할인";
    public static final String DISCOUNT_TYPE_FIXED = "FIXED_PRICE";
    public static final long FIXED_DISCOUNT_AMOUNT = 5000L;

    /** 비활성 할인 정책 */
    public static final Long INACTIVE_DISCOUNT_ID = 3L;

    public static final String INACTIVE_DISCOUNT_NAME = "비활성 할인";

    /** 삭제된 할인 정책 */
    public static final Long DELETED_DISCOUNT_ID = 4L;

    /** 기간 만료된 할인 정책 */
    public static final Long EXPIRED_DISCOUNT_ID = 5L;

    /** 존재하지 않는 할인 정책 ID */
    public static final Long NON_EXISTENT_DISCOUNT_ID = 9999L;

    /** 비용 분담 비율 */
    public static final BigDecimal PLATFORM_COST_SHARE_RATIO = new BigDecimal("50.00");

    public static final BigDecimal SELLER_COST_SHARE_RATIO = new BigDecimal("50.00");

    /** 우선순위 */
    public static final int DEFAULT_PRIORITY = 100;

    /** 테스트 데이터 개수 */
    public static final int TOTAL_ACTIVE_POLICIES = 2;

    public static final int TOTAL_VALID_POLICIES = 2;
    public static final int TOTAL_ALL_POLICIES = 5;

    // ============================================================
    // API Paths
    // ============================================================

    /** Admin API 기본 경로 */
    public static final String ADMIN_API_V2 = "/api/v2/admin";

    /** 할인 정책 API 경로 (sellerId PathVariable 필요) */
    public static String discountPoliciesPath(Long sellerId) {
        return ADMIN_API_V2 + "/sellers/" + sellerId + "/discount-policies";
    }

    /** 할인 정책 상세 경로 */
    public static String discountPolicyDetailPath(Long sellerId, Long discountPolicyId) {
        return discountPoliciesPath(sellerId) + "/" + discountPolicyId;
    }

    /** 현재 유효한 할인 정책 경로 */
    public static String validDiscountPoliciesPath(Long sellerId) {
        return discountPoliciesPath(sellerId) + "/valid";
    }

    /** 할인 정책 활성화 경로 */
    public static String activateDiscountPolicyPath(Long sellerId, Long discountPolicyId) {
        return discountPolicyDetailPath(sellerId, discountPolicyId) + "/activate";
    }

    /** 할인 정책 비활성화 경로 */
    public static String deactivateDiscountPolicyPath(Long sellerId, Long discountPolicyId) {
        return discountPolicyDetailPath(sellerId, discountPolicyId) + "/deactivate";
    }

    /** 할인 정책 삭제 경로 */
    public static String deleteDiscountPolicyPath(Long sellerId, Long discountPolicyId) {
        return discountPolicyDetailPath(sellerId, discountPolicyId) + "/delete";
    }

    /** 할인 정책 적용 대상 수정 경로 */
    public static String discountTargetsPath(Long sellerId, Long discountPolicyId) {
        return discountPolicyDetailPath(sellerId, discountPolicyId) + "/targets";
    }

    // ============================================================
    // Request Builders
    // ============================================================

    /**
     * 정률 할인 정책 등록 요청 생성
     *
     * @param policyName 정책명
     * @param discountRate 할인율 (1-100)
     * @return 등록 요청 DTO
     */
    public static RegisterDiscountPolicyV2ApiRequest createRateDiscountRequest(
            String policyName, int discountRate) {
        Instant now = Instant.now();
        return new RegisterDiscountPolicyV2ApiRequest(
                policyName,
                DISCOUNT_GROUP_PRODUCT,
                DISCOUNT_TYPE_RATE,
                TARGET_TYPE_ALL,
                null, // targetId
                discountRate,
                null, // fixedDiscountAmount
                RATE_DISCOUNT_MAX_AMOUNT,
                RATE_DISCOUNT_MIN_ORDER,
                now,
                now.plus(365, ChronoUnit.DAYS),
                1, // maxUsagePerCustomer
                1000, // maxTotalUsage
                PLATFORM_COST_SHARE_RATIO,
                SELLER_COST_SHARE_RATIO,
                DEFAULT_PRIORITY,
                true);
    }

    /**
     * 정액 할인 정책 등록 요청 생성
     *
     * @param policyName 정책명
     * @param discountAmount 할인 금액
     * @return 등록 요청 DTO
     */
    public static RegisterDiscountPolicyV2ApiRequest createFixedDiscountRequest(
            String policyName, long discountAmount) {
        Instant now = Instant.now();
        return new RegisterDiscountPolicyV2ApiRequest(
                policyName,
                DISCOUNT_GROUP_PRODUCT,
                DISCOUNT_TYPE_FIXED,
                TARGET_TYPE_ALL,
                null, // targetId
                null, // discountRate
                discountAmount,
                null, // maximumDiscountAmount (정액 할인은 불필요)
                RATE_DISCOUNT_MIN_ORDER,
                now,
                now.plus(365, ChronoUnit.DAYS),
                1, // maxUsagePerCustomer
                1000, // maxTotalUsage
                PLATFORM_COST_SHARE_RATIO,
                SELLER_COST_SHARE_RATIO,
                DEFAULT_PRIORITY,
                true);
    }

    /**
     * 기본 정률 할인 정책 등록 요청 생성
     *
     * @return 등록 요청 DTO
     */
    public static RegisterDiscountPolicyV2ApiRequest createDefaultRateDiscountRequest() {
        return createRateDiscountRequest("통합테스트 10% 할인", 10);
    }

    /**
     * 기본 정액 할인 정책 등록 요청 생성
     *
     * @return 등록 요청 DTO
     */
    public static RegisterDiscountPolicyV2ApiRequest createDefaultFixedDiscountRequest() {
        return createFixedDiscountRequest("통합테스트 5000원 할인", 5000L);
    }

    /**
     * 할인 정책 수정 요청 생성
     *
     * @param newPolicyName 새 정책명 (nullable)
     * @param newMaximumDiscountAmount 새 최대 할인 금액 (nullable)
     * @return 수정 요청 DTO
     */
    public static UpdateDiscountPolicyV2ApiRequest createUpdateRequest(
            String newPolicyName, Long newMaximumDiscountAmount) {
        return new UpdateDiscountPolicyV2ApiRequest(
                newPolicyName,
                newMaximumDiscountAmount,
                null, // minimumOrderAmount
                null, // validEndAt
                null, // maxUsagePerCustomer
                null, // maxTotalUsage
                null, // platformCostShareRatio
                null, // sellerCostShareRatio
                null); // priority
    }

    /**
     * 적용 대상 수정 요청 생성
     *
     * @param targetIds 새 적용 대상 ID 목록
     * @return 수정 요청 DTO
     */
    public static UpdateDiscountTargetsV2ApiRequest createUpdateTargetsRequest(
            List<Long> targetIds) {
        return new UpdateDiscountTargetsV2ApiRequest(targetIds);
    }

    // ============================================================
    // Validation Test Data
    // ============================================================

    /**
     * 필수값 누락 요청 (정책명 없음)
     *
     * @return 유효하지 않은 등록 요청 DTO
     */
    public static RegisterDiscountPolicyV2ApiRequest createInvalidRequestMissingPolicyName() {
        Instant now = Instant.now();
        return new RegisterDiscountPolicyV2ApiRequest(
                null, // policyName - 필수값 누락
                DISCOUNT_GROUP_PRODUCT,
                DISCOUNT_TYPE_RATE,
                TARGET_TYPE_ALL,
                null,
                10,
                null,
                RATE_DISCOUNT_MAX_AMOUNT,
                RATE_DISCOUNT_MIN_ORDER,
                now,
                now.plus(365, ChronoUnit.DAYS),
                1,
                1000,
                PLATFORM_COST_SHARE_RATIO,
                SELLER_COST_SHARE_RATIO,
                DEFAULT_PRIORITY,
                true);
    }

    /**
     * 잘못된 할인율 요청 (0% 미만)
     *
     * @return 유효하지 않은 등록 요청 DTO
     */
    public static RegisterDiscountPolicyV2ApiRequest createInvalidRequestNegativeRate() {
        Instant now = Instant.now();
        return new RegisterDiscountPolicyV2ApiRequest(
                "잘못된 할인율 테스트",
                DISCOUNT_GROUP_PRODUCT,
                DISCOUNT_TYPE_RATE,
                TARGET_TYPE_ALL,
                null,
                -10, // 음수 할인율
                null,
                RATE_DISCOUNT_MAX_AMOUNT,
                RATE_DISCOUNT_MIN_ORDER,
                now,
                now.plus(365, ChronoUnit.DAYS),
                1,
                1000,
                PLATFORM_COST_SHARE_RATIO,
                SELLER_COST_SHARE_RATIO,
                DEFAULT_PRIORITY,
                true);
    }

    /**
     * 잘못된 할인율 요청 (100% 초과)
     *
     * @return 유효하지 않은 등록 요청 DTO
     */
    public static RegisterDiscountPolicyV2ApiRequest createInvalidRequestExcessiveRate() {
        Instant now = Instant.now();
        return new RegisterDiscountPolicyV2ApiRequest(
                "100% 초과 할인율 테스트",
                DISCOUNT_GROUP_PRODUCT,
                DISCOUNT_TYPE_RATE,
                TARGET_TYPE_ALL,
                null,
                150, // 100% 초과
                null,
                RATE_DISCOUNT_MAX_AMOUNT,
                RATE_DISCOUNT_MIN_ORDER,
                now,
                now.plus(365, ChronoUnit.DAYS),
                1,
                1000,
                PLATFORM_COST_SHARE_RATIO,
                SELLER_COST_SHARE_RATIO,
                DEFAULT_PRIORITY,
                true);
    }
}
