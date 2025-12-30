package com.ryuqq.setof.application.discount.assembler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyResponse;
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

/**
 * DiscountPolicyAssembler 테스트
 *
 * <p>Domain → Response 변환에 대한 단위 테스트
 *
 * <p>주의: toDomain 테스트는 DiscountPolicyCommandFactoryTest에서 진행합니다.
 */
@DisplayName("DiscountPolicyAssembler")
class DiscountPolicyAssemblerTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Instant FUTURE_TIME = Instant.parse("2025-12-31T23:59:59Z");
    private static final Long TEST_SELLER_ID = 1L;

    private DiscountPolicyAssembler discountPolicyAssembler;

    @BeforeEach
    void setUp() {
        discountPolicyAssembler = new DiscountPolicyAssembler();
    }

    @Nested
    @DisplayName("toResponse - 정률 할인")
    class ToResponseRateDiscountTest {

        @Test
        @DisplayName("정률 할인 정책을 Response로 변환")
        void shouldConvertRateDiscountToResponse() {
            // Given
            DiscountPolicy discountPolicy = createRateDiscountPolicy();

            // When
            DiscountPolicyResponse response = discountPolicyAssembler.toResponse(discountPolicy);

            // Then
            assertNotNull(response);
            assertEquals(1L, response.discountPolicyId());
            assertEquals(TEST_SELLER_ID, response.sellerId());
            assertEquals("여름 시즌 할인", response.policyName());
            assertEquals(DiscountGroup.PRODUCT, response.discountGroup());
            assertEquals(DiscountType.RATE, response.discountType());
            assertEquals(DiscountTargetType.ALL, response.discountTargetType());
            assertNull(response.targetId());
            assertThat(response.discountRate()).isEqualByComparingTo(BigDecimal.valueOf(10));
            assertNull(response.fixedDiscountAmount());
            assertEquals(10000L, response.maximumDiscountAmount());
            assertEquals(30000L, response.minimumOrderAmount());
            assertEquals(1, response.maxUsagePerCustomer());
            assertEquals(1000, response.maxTotalUsage());
            assertThat(response.platformCostShareRatio())
                    .isEqualByComparingTo(new BigDecimal("50"));
            assertThat(response.sellerCostShareRatio()).isEqualByComparingTo(new BigDecimal("50"));
            assertEquals(100, response.priority());
            assertTrue(response.isActive());
            assertFalse(response.isDeleted());
        }

        @Test
        @DisplayName("특정 대상 ID가 있는 정률 할인 정책 변환")
        void shouldConvertRateDiscountWithTargetIdToResponse() {
            // Given
            DiscountPolicy discountPolicy = createRateDiscountPolicyWithTargetId(100L);

            // When
            DiscountPolicyResponse response = discountPolicyAssembler.toResponse(discountPolicy);

            // Then
            assertNotNull(response);
            assertEquals(DiscountTargetType.PRODUCT, response.discountTargetType());
            assertEquals(100L, response.targetId());
        }
    }

    @Nested
    @DisplayName("toResponse - 정액 할인")
    class ToResponseFixedDiscountTest {

        @Test
        @DisplayName("정액 할인 정책을 Response로 변환")
        void shouldConvertFixedDiscountToResponse() {
            // Given
            DiscountPolicy discountPolicy = createFixedDiscountPolicy();

            // When
            DiscountPolicyResponse response = discountPolicyAssembler.toResponse(discountPolicy);

            // Then
            assertNotNull(response);
            assertEquals(2L, response.discountPolicyId());
            assertEquals(TEST_SELLER_ID, response.sellerId());
            assertEquals("정액 할인 이벤트", response.policyName());
            assertEquals(DiscountGroup.MEMBER, response.discountGroup());
            assertEquals(DiscountType.FIXED_PRICE, response.discountType());
            assertNull(response.discountRate());
            assertEquals(5000L, response.fixedDiscountAmount());
            assertEquals(50000L, response.minimumOrderAmount());
            assertEquals(200, response.priority());
        }
    }

    @Nested
    @DisplayName("toResponses")
    class ToResponsesTest {

        @Test
        @DisplayName("Domain 목록을 Response 목록으로 변환")
        void shouldConvertDomainListToResponseList() {
            // Given
            List<DiscountPolicy> policies =
                    List.of(createRateDiscountPolicy(), createFixedDiscountPolicy());

            // When
            List<DiscountPolicyResponse> responses = discountPolicyAssembler.toResponses(policies);

            // Then
            assertNotNull(responses);
            assertEquals(2, responses.size());
            assertEquals(1L, responses.get(0).discountPolicyId());
            assertEquals(2L, responses.get(1).discountPolicyId());
            assertEquals(DiscountType.RATE, responses.get(0).discountType());
            assertEquals(DiscountType.FIXED_PRICE, responses.get(1).discountType());
        }

        @Test
        @DisplayName("빈 목록 변환 시 빈 Response 목록 반환")
        void shouldReturnEmptyListForEmptyInput() {
            // Given
            List<DiscountPolicy> policies = List.of();

            // When
            List<DiscountPolicyResponse> responses = discountPolicyAssembler.toResponses(policies);

            // Then
            assertNotNull(responses);
            assertTrue(responses.isEmpty());
        }
    }

    @Nested
    @DisplayName("toResponse - 선택 필드 처리")
    class ToResponseOptionalFieldsTest {

        @Test
        @DisplayName("무제한 사용 횟수 정책 변환")
        void shouldConvertPolicyWithUnlimitedUsage() {
            // Given
            DiscountPolicy discountPolicy = createPolicyWithUnlimitedUsage();

            // When
            DiscountPolicyResponse response = discountPolicyAssembler.toResponse(discountPolicy);

            // Then
            assertNotNull(response);
            assertNull(response.maxUsagePerCustomer());
            assertNull(response.maxTotalUsage());
        }

        @Test
        @DisplayName("최소 주문 금액 없는 정책 변환")
        void shouldConvertPolicyWithNoMinimumOrderAmount() {
            // Given
            DiscountPolicy discountPolicy = createPolicyWithNoMinimumOrderAmount();

            // When
            DiscountPolicyResponse response = discountPolicyAssembler.toResponse(discountPolicy);

            // Then
            assertNotNull(response);
            assertEquals(0L, response.minimumOrderAmount());
        }
    }

    // ========== Helper Methods ==========

    private DiscountPolicy createRateDiscountPolicy() {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(1L),
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

    private DiscountPolicy createRateDiscountPolicyWithTargetId(Long targetId) {
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

    private DiscountPolicy createPolicyWithUnlimitedUsage() {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(4L),
                TEST_SELLER_ID,
                PolicyName.of("무제한 할인"),
                DiscountGroup.PRODUCT,
                DiscountType.RATE,
                DiscountTargetType.ALL,
                List.of(),
                DiscountRate.of(5),
                null,
                MaximumDiscountAmount.unlimited(),
                MinimumOrderAmount.noMinimum(),
                ValidPeriod.of(FIXED_TIME, FUTURE_TIME),
                UsageLimit.unlimited(),
                CostShare.of(new BigDecimal("100"), new BigDecimal("0")),
                Priority.of(500),
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private DiscountPolicy createPolicyWithNoMinimumOrderAmount() {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(5L),
                TEST_SELLER_ID,
                PolicyName.of("최소 금액 없음"),
                DiscountGroup.PRODUCT,
                DiscountType.FIXED_PRICE,
                DiscountTargetType.ALL,
                List.of(),
                null,
                DiscountAmount.of(1000L),
                null,
                MinimumOrderAmount.noMinimum(),
                ValidPeriod.of(FIXED_TIME, FUTURE_TIME),
                UsageLimit.of(1, 100),
                CostShare.of(new BigDecimal("50"), new BigDecimal("50")),
                Priority.of(300),
                true,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }
}
