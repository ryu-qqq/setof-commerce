package com.ryuqq.setof.application.discount.factory.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.discount.dto.command.RegisterDiscountPolicyCommand;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.vo.DiscountGroup;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.DiscountType;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * DiscountPolicyCommandFactory 테스트
 *
 * <p>Command → Domain 변환에 대한 단위 테스트
 */
@DisplayName("DiscountPolicyCommandFactory")
@ExtendWith(MockitoExtension.class)
class DiscountPolicyCommandFactoryTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Clock FIXED_CLOCK = Clock.fixed(FIXED_TIME, ZoneId.of("UTC"));
    private static final Long TEST_SELLER_ID = 1L;

    @Mock private ClockHolder clockHolder;

    private DiscountPolicyCommandFactory discountPolicyCommandFactory;

    @BeforeEach
    void setUp() {
        discountPolicyCommandFactory = new DiscountPolicyCommandFactory(clockHolder);
    }

    @Nested
    @DisplayName("create - 정률 할인")
    class CreateRateDiscountTest {

        @Test
        @DisplayName("모든 필드가 있는 정률 할인 정책 생성 성공")
        void shouldCreateRateDiscountWithAllFields() {
            // Given
            RegisterDiscountPolicyCommand command = createRateDiscountCommand();
            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            DiscountPolicy discountPolicy = discountPolicyCommandFactory.create(command);

            // Then
            assertNotNull(discountPolicy);
            assertEquals(TEST_SELLER_ID, discountPolicy.getSellerId());
            assertEquals("여름 시즌 할인", discountPolicy.getPolicyNameValue());
            assertEquals(DiscountGroup.PRODUCT, discountPolicy.getDiscountGroup());
            assertEquals(DiscountType.RATE, discountPolicy.getDiscountType());
            assertEquals(DiscountTargetType.ALL, discountPolicy.getTargetType());
            assertThat(discountPolicy.getDiscountRateValue())
                    .isEqualByComparingTo(BigDecimal.valueOf(10));
            assertEquals(10000L, discountPolicy.getMaximumDiscountAmountValue());
            assertEquals(30000L, discountPolicy.getMinimumOrderAmountValue());
            assertEquals(1, discountPolicy.getMaxUsagePerCustomer());
            assertEquals(1000, discountPolicy.getMaxTotalUsage());
            assertEquals(100, discountPolicy.getPriorityValue());
            assertTrue(discountPolicy.isActive());
            assertFalse(discountPolicy.isDeleted());
        }

        @Test
        @DisplayName("최소 필드로 정률 할인 정책 생성 성공")
        void shouldCreateRateDiscountWithMinimalFields() {
            // Given
            RegisterDiscountPolicyCommand command = createMinimalRateDiscountCommand();
            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            DiscountPolicy discountPolicy = discountPolicyCommandFactory.create(command);

            // Then
            assertNotNull(discountPolicy);
            assertEquals(TEST_SELLER_ID, discountPolicy.getSellerId());
            assertEquals("기본 할인", discountPolicy.getPolicyNameValue());
            assertEquals(DiscountType.RATE, discountPolicy.getDiscountType());
            assertThat(discountPolicy.getDiscountRateValue())
                    .isEqualByComparingTo(BigDecimal.valueOf(5));
            assertNull(discountPolicy.getMaximumDiscountAmountValue());
            assertEquals(0L, discountPolicy.getMinimumOrderAmountValue());
            assertNull(discountPolicy.getMaxUsagePerCustomer());
            assertNull(discountPolicy.getMaxTotalUsage());
        }

        @Test
        @DisplayName("특정 대상 ID가 있는 정률 할인 정책 생성")
        void shouldCreateRateDiscountWithTargetId() {
            // Given
            RegisterDiscountPolicyCommand command = createRateDiscountCommandWithTargetId(100L);
            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            DiscountPolicy discountPolicy = discountPolicyCommandFactory.create(command);

            // Then
            assertNotNull(discountPolicy);
            assertEquals(DiscountTargetType.PRODUCT, discountPolicy.getTargetType());
            assertEquals(100L, discountPolicy.getTargetId());
        }
    }

    @Nested
    @DisplayName("create - 정액 할인")
    class CreateFixedDiscountTest {

        @Test
        @DisplayName("모든 필드가 있는 정액 할인 정책 생성 성공")
        void shouldCreateFixedDiscountWithAllFields() {
            // Given
            RegisterDiscountPolicyCommand command = createFixedDiscountCommand();
            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            DiscountPolicy discountPolicy = discountPolicyCommandFactory.create(command);

            // Then
            assertNotNull(discountPolicy);
            assertEquals(TEST_SELLER_ID, discountPolicy.getSellerId());
            assertEquals("정액 할인 이벤트", discountPolicy.getPolicyNameValue());
            assertEquals(DiscountGroup.MEMBER, discountPolicy.getDiscountGroup());
            assertEquals(DiscountType.FIXED_PRICE, discountPolicy.getDiscountType());
            assertEquals(DiscountTargetType.ALL, discountPolicy.getTargetType());
            assertEquals(5000L, discountPolicy.getDiscountAmountValue());
            assertEquals(50000L, discountPolicy.getMinimumOrderAmountValue());
            assertEquals(200, discountPolicy.getPriorityValue());
        }

        @Test
        @DisplayName("최소 필드로 정액 할인 정책 생성 성공")
        void shouldCreateFixedDiscountWithMinimalFields() {
            // Given
            RegisterDiscountPolicyCommand command = createMinimalFixedDiscountCommand();
            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            DiscountPolicy discountPolicy = discountPolicyCommandFactory.create(command);

            // Then
            assertNotNull(discountPolicy);
            assertEquals("기본 정액 할인", discountPolicy.getPolicyNameValue());
            assertEquals(DiscountType.FIXED_PRICE, discountPolicy.getDiscountType());
            assertEquals(1000L, discountPolicy.getDiscountAmountValue());
            assertEquals(0L, discountPolicy.getMinimumOrderAmountValue());
        }
    }

    @Nested
    @DisplayName("create - 비용 분담")
    class CreateCostShareTest {

        @Test
        @DisplayName("비용 분담 비율이 올바르게 설정된다")
        void shouldSetCostShareRatioCorrectly() {
            // Given
            RegisterDiscountPolicyCommand command = createRateDiscountCommand();
            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            DiscountPolicy discountPolicy = discountPolicyCommandFactory.create(command);

            // Then
            assertThat(discountPolicy.getPlatformCostShareRatio())
                    .isEqualByComparingTo(new BigDecimal("50"));
            assertThat(discountPolicy.getSellerCostShareRatio())
                    .isEqualByComparingTo(new BigDecimal("50"));
        }
    }

    // ========== Helper Methods ==========

    private RegisterDiscountPolicyCommand createRateDiscountCommand() {
        return new RegisterDiscountPolicyCommand(
                TEST_SELLER_ID,
                "여름 시즌 할인",
                DiscountGroup.PRODUCT,
                DiscountType.RATE,
                DiscountTargetType.ALL,
                null,
                10,
                null,
                10000L,
                30000L,
                Instant.parse("2025-06-01T00:00:00Z"),
                Instant.parse("2025-08-31T23:59:59Z"),
                1,
                1000,
                new BigDecimal("50"),
                new BigDecimal("50"),
                100,
                true);
    }

    private RegisterDiscountPolicyCommand createMinimalRateDiscountCommand() {
        return new RegisterDiscountPolicyCommand(
                TEST_SELLER_ID,
                "기본 할인",
                DiscountGroup.PRODUCT,
                DiscountType.RATE,
                DiscountTargetType.ALL,
                null,
                5,
                null,
                null,
                null,
                Instant.parse("2025-01-01T00:00:00Z"),
                Instant.parse("2025-12-31T23:59:59Z"),
                null,
                null,
                new BigDecimal("100"),
                new BigDecimal("0"),
                500,
                true);
    }

    private RegisterDiscountPolicyCommand createRateDiscountCommandWithTargetId(Long targetId) {
        return new RegisterDiscountPolicyCommand(
                TEST_SELLER_ID,
                "상품별 할인",
                DiscountGroup.PRODUCT,
                DiscountType.RATE,
                DiscountTargetType.PRODUCT,
                targetId,
                15,
                null,
                20000L,
                10000L,
                Instant.parse("2025-01-01T00:00:00Z"),
                Instant.parse("2025-12-31T23:59:59Z"),
                null,
                null,
                new BigDecimal("30"),
                new BigDecimal("70"),
                50,
                true);
    }

    private RegisterDiscountPolicyCommand createFixedDiscountCommand() {
        return new RegisterDiscountPolicyCommand(
                TEST_SELLER_ID,
                "정액 할인 이벤트",
                DiscountGroup.MEMBER,
                DiscountType.FIXED_PRICE,
                DiscountTargetType.ALL,
                null,
                null,
                5000L,
                null,
                50000L,
                Instant.parse("2025-03-01T00:00:00Z"),
                Instant.parse("2025-03-31T23:59:59Z"),
                2,
                500,
                new BigDecimal("60"),
                new BigDecimal("40"),
                200,
                true);
    }

    private RegisterDiscountPolicyCommand createMinimalFixedDiscountCommand() {
        return new RegisterDiscountPolicyCommand(
                TEST_SELLER_ID,
                "기본 정액 할인",
                DiscountGroup.PRODUCT,
                DiscountType.FIXED_PRICE,
                DiscountTargetType.ALL,
                null,
                null,
                1000L,
                null,
                null,
                Instant.parse("2025-01-01T00:00:00Z"),
                Instant.parse("2025-12-31T23:59:59Z"),
                null,
                null,
                new BigDecimal("100"),
                new BigDecimal("0"),
                999,
                false);
    }
}
