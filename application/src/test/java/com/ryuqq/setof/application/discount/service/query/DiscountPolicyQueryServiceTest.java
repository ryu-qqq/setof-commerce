package com.ryuqq.setof.application.discount.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.discount.assembler.DiscountPolicyAssembler;
import com.ryuqq.setof.application.discount.dto.query.DiscountPolicySearchQuery;
import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyResponse;
import com.ryuqq.setof.application.discount.manager.query.DiscountPolicyReadManager;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.vo.CostShare;
import com.ryuqq.setof.domain.discount.vo.DiscountAmount;
import com.ryuqq.setof.domain.discount.vo.DiscountGroup;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyId;
import com.ryuqq.setof.domain.discount.vo.DiscountRate;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.DiscountType;
import com.ryuqq.setof.domain.discount.vo.MaximumDiscountAmount;
import com.ryuqq.setof.domain.discount.vo.MinimumOrderAmount;
import com.ryuqq.setof.domain.discount.vo.PolicyName;
import com.ryuqq.setof.domain.discount.vo.Priority;
import com.ryuqq.setof.domain.discount.vo.UsageLimit;
import com.ryuqq.setof.domain.discount.vo.ValidPeriod;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * DiscountPolicyQueryService 테스트
 *
 * <p>할인 정책 조회 서비스에 대한 단위 테스트
 */
@DisplayName("DiscountPolicyQueryService")
@ExtendWith(MockitoExtension.class)
class DiscountPolicyQueryServiceTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Instant FUTURE_TIME = Instant.parse("2025-12-31T23:59:59Z");
    private static final Long TEST_SELLER_ID = 1L;
    private static final Long TEST_POLICY_ID = 1L;

    @Mock private DiscountPolicyReadManager discountPolicyReadManager;

    private DiscountPolicyAssembler discountPolicyAssembler;
    private DiscountPolicyQueryService discountPolicyQueryService;

    @BeforeEach
    void setUp() {
        discountPolicyAssembler = new DiscountPolicyAssembler();
        discountPolicyQueryService =
                new DiscountPolicyQueryService(discountPolicyReadManager, discountPolicyAssembler);
    }

    @Nested
    @DisplayName("execute(Long discountPolicyId)")
    class GetSinglePolicyTest {

        @Test
        @DisplayName("ID로 정책 조회 성공")
        void shouldGetPolicyByIdSuccessfully() {
            // Given
            DiscountPolicy policy = createRateDiscountPolicy();
            when(discountPolicyReadManager.findById(eq(TEST_POLICY_ID))).thenReturn(policy);

            // When
            DiscountPolicyResponse response = discountPolicyQueryService.execute(TEST_POLICY_ID);

            // Then
            assertNotNull(response);
            assertEquals(TEST_POLICY_ID, response.discountPolicyId());
            assertEquals(TEST_SELLER_ID, response.sellerId());
            assertEquals("여름 시즌 할인", response.policyName());
            assertEquals(DiscountGroup.PRODUCT, response.discountGroup());
            assertEquals(DiscountType.RATE, response.discountType());
            verify(discountPolicyReadManager).findById(TEST_POLICY_ID);
        }
    }

    @Nested
    @DisplayName("execute(DiscountPolicySearchQuery)")
    class GetPoliciesByQueryTest {

        @Test
        @DisplayName("셀러 ID로 전체 정책 조회 성공")
        void shouldGetAllPoliciesBySellerIdSuccessfully() {
            // Given
            List<DiscountPolicy> policies =
                    List.of(createRateDiscountPolicy(), createFixedDiscountPolicy());
            DiscountPolicySearchQuery query =
                    DiscountPolicySearchQuery.forAllPolicies(TEST_SELLER_ID);

            when(discountPolicyReadManager.findBySellerId(eq(TEST_SELLER_ID), eq(false)))
                    .thenReturn(policies);

            // When
            List<DiscountPolicyResponse> responses = discountPolicyQueryService.execute(query);

            // Then
            assertNotNull(responses);
            assertEquals(2, responses.size());
            verify(discountPolicyReadManager).findBySellerId(TEST_SELLER_ID, false);
        }

        @Test
        @DisplayName("활성 정책만 조회 성공")
        void shouldGetActivePoliciesSuccessfully() {
            // Given
            List<DiscountPolicy> policies = List.of(createRateDiscountPolicy());
            DiscountPolicySearchQuery query =
                    DiscountPolicySearchQuery.forActivePolicies(TEST_SELLER_ID);

            when(discountPolicyReadManager.findValidPolicies(eq(TEST_SELLER_ID)))
                    .thenReturn(policies);

            // When
            List<DiscountPolicyResponse> responses = discountPolicyQueryService.execute(query);

            // Then
            assertNotNull(responses);
            assertEquals(1, responses.size());
            assertTrue(responses.get(0).isActive());
            verify(discountPolicyReadManager).findValidPolicies(TEST_SELLER_ID);
        }

        @Test
        @DisplayName("특정 그룹의 정책 조회 성공")
        void shouldGetPoliciesByGroupSuccessfully() {
            // Given
            List<DiscountPolicy> policies = List.of(createRateDiscountPolicy());
            DiscountPolicySearchQuery query =
                    DiscountPolicySearchQuery.forGroup(TEST_SELLER_ID, DiscountGroup.PRODUCT);

            when(discountPolicyReadManager.findBySellerIdAndGroup(
                            eq(TEST_SELLER_ID), eq(DiscountGroup.PRODUCT), eq(true)))
                    .thenReturn(policies);

            // When
            List<DiscountPolicyResponse> responses = discountPolicyQueryService.execute(query);

            // Then
            assertNotNull(responses);
            assertEquals(1, responses.size());
            assertEquals(DiscountGroup.PRODUCT, responses.get(0).discountGroup());
            verify(discountPolicyReadManager)
                    .findBySellerIdAndGroup(TEST_SELLER_ID, DiscountGroup.PRODUCT, true);
        }

        @Test
        @DisplayName("빈 결과 조회")
        void shouldReturnEmptyListWhenNoPoliciesFound() {
            // Given
            DiscountPolicySearchQuery query =
                    DiscountPolicySearchQuery.forAllPolicies(TEST_SELLER_ID);

            when(discountPolicyReadManager.findBySellerId(eq(TEST_SELLER_ID), eq(false)))
                    .thenReturn(List.of());

            // When
            List<DiscountPolicyResponse> responses = discountPolicyQueryService.execute(query);

            // Then
            assertNotNull(responses);
            assertTrue(responses.isEmpty());
        }

        @Test
        @DisplayName("할인 타입 필터 적용")
        void shouldFilterByDiscountType() {
            // Given
            List<DiscountPolicy> policies =
                    List.of(createRateDiscountPolicy(), createFixedDiscountPolicy());
            DiscountPolicySearchQuery query =
                    new DiscountPolicySearchQuery(
                            TEST_SELLER_ID,
                            null,
                            DiscountType.RATE,
                            null,
                            null,
                            false,
                            false,
                            false);

            when(discountPolicyReadManager.findBySellerId(eq(TEST_SELLER_ID), eq(false)))
                    .thenReturn(policies);

            // When
            List<DiscountPolicyResponse> responses = discountPolicyQueryService.execute(query);

            // Then
            assertNotNull(responses);
            assertEquals(1, responses.size());
            assertEquals(DiscountType.RATE, responses.get(0).discountType());
        }

        @Test
        @DisplayName("대상 타입 필터 적용")
        void shouldFilterByTargetType() {
            // Given
            List<DiscountPolicy> policies =
                    List.of(createRateDiscountPolicy(), createPolicyWithTargetId(100L));
            DiscountPolicySearchQuery query =
                    new DiscountPolicySearchQuery(
                            TEST_SELLER_ID,
                            null,
                            null,
                            DiscountTargetType.PRODUCT,
                            null,
                            false,
                            false,
                            false);

            when(discountPolicyReadManager.findBySellerId(eq(TEST_SELLER_ID), eq(false)))
                    .thenReturn(policies);

            // When
            List<DiscountPolicyResponse> responses = discountPolicyQueryService.execute(query);

            // Then
            assertNotNull(responses);
            assertEquals(1, responses.size());
            assertEquals(DiscountTargetType.PRODUCT, responses.get(0).discountTargetType());
        }
    }

    // ========== Helper Methods ==========

    private DiscountPolicy createRateDiscountPolicy() {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(TEST_POLICY_ID),
                TEST_SELLER_ID,
                PolicyName.of("여름 시즌 할인"),
                DiscountGroup.PRODUCT,
                DiscountType.RATE,
                DiscountTargetType.ALL,
                List.of(),
                DiscountRate.of(10),
                null,
                MaximumDiscountAmount.of(10000L),
                MinimumOrderAmount.of(30000L),
                ValidPeriod.of(FIXED_TIME, FUTURE_TIME),
                UsageLimit.of(1, 1000),
                CostShare.of(new BigDecimal("50"), new BigDecimal("50")),
                Priority.of(100),
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private DiscountPolicy createFixedDiscountPolicy() {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(2L),
                TEST_SELLER_ID,
                PolicyName.of("정액 할인 이벤트"),
                DiscountGroup.MEMBER,
                DiscountType.FIXED_PRICE,
                DiscountTargetType.ALL,
                List.of(),
                null,
                DiscountAmount.of(5000L),
                null,
                MinimumOrderAmount.of(50000L),
                ValidPeriod.of(FIXED_TIME, FUTURE_TIME),
                UsageLimit.of(2, 500),
                CostShare.of(new BigDecimal("60"), new BigDecimal("40")),
                Priority.of(200),
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private DiscountPolicy createPolicyWithTargetId(Long targetId) {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(3L),
                TEST_SELLER_ID,
                PolicyName.of("상품별 할인"),
                DiscountGroup.PRODUCT,
                DiscountType.RATE,
                DiscountTargetType.PRODUCT,
                List.of(targetId),
                DiscountRate.of(15),
                null,
                MaximumDiscountAmount.of(20000L),
                MinimumOrderAmount.of(10000L),
                ValidPeriod.of(FIXED_TIME, FUTURE_TIME),
                UsageLimit.unlimited(),
                CostShare.of(new BigDecimal("30"), new BigDecimal("70")),
                Priority.of(50),
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }
}
