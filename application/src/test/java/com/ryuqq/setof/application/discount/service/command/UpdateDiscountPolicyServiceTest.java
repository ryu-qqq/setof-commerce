package com.ryuqq.setof.application.discount.service.command;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.manager.command.DiscountPolicyPersistenceManager;
import com.ryuqq.setof.application.discount.manager.query.DiscountPolicyReadManager;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.vo.CostShare;
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
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * UpdateDiscountPolicyService 테스트
 *
 * <p>할인 정책 수정 서비스에 대한 단위 테스트
 */
@DisplayName("UpdateDiscountPolicyService")
@ExtendWith(MockitoExtension.class)
class UpdateDiscountPolicyServiceTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Instant FUTURE_TIME = Instant.parse("2025-12-31T23:59:59Z");
    private static final Clock FIXED_CLOCK = Clock.fixed(FIXED_TIME, ZoneId.of("UTC"));
    private static final Long TEST_SELLER_ID = 1L;
    private static final Long TEST_POLICY_ID = 1L;

    @Mock private DiscountPolicyReadManager discountPolicyReadManager;
    @Mock private DiscountPolicyPersistenceManager discountPolicyPersistenceManager;
    @Mock private ClockHolder clockHolder;

    private UpdateDiscountPolicyService updateDiscountPolicyService;

    @BeforeEach
    void setUp() {
        updateDiscountPolicyService =
                new UpdateDiscountPolicyService(
                        discountPolicyReadManager, discountPolicyPersistenceManager, clockHolder);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("정책명 수정 성공")
        void shouldUpdatePolicyNameSuccessfully() {
            // Given
            DiscountPolicy existingPolicy = createExistingPolicy();
            UpdateDiscountPolicyCommand command = createPolicyNameUpdateCommand();

            when(discountPolicyReadManager.findById(eq(TEST_POLICY_ID))).thenReturn(existingPolicy);
            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            updateDiscountPolicyService.execute(command);

            // Then
            verify(discountPolicyReadManager).findById(TEST_POLICY_ID);
            verify(discountPolicyPersistenceManager).persist(any(DiscountPolicy.class));
        }

        @Test
        @DisplayName("최대 할인 금액 수정 성공")
        void shouldUpdateMaximumDiscountAmountSuccessfully() {
            // Given
            DiscountPolicy existingPolicy = createExistingPolicy();
            UpdateDiscountPolicyCommand command = createMaximumDiscountAmountUpdateCommand();

            when(discountPolicyReadManager.findById(eq(TEST_POLICY_ID))).thenReturn(existingPolicy);
            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            updateDiscountPolicyService.execute(command);

            // Then
            verify(discountPolicyReadManager).findById(TEST_POLICY_ID);
            verify(discountPolicyPersistenceManager).persist(any(DiscountPolicy.class));
        }

        @Test
        @DisplayName("유효 기간 연장 성공")
        void shouldExtendValidPeriodSuccessfully() {
            // Given
            DiscountPolicy existingPolicy = createExistingPolicy();
            UpdateDiscountPolicyCommand command = createValidPeriodExtensionCommand();

            when(discountPolicyReadManager.findById(eq(TEST_POLICY_ID))).thenReturn(existingPolicy);
            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            updateDiscountPolicyService.execute(command);

            // Then
            verify(discountPolicyReadManager).findById(TEST_POLICY_ID);
            verify(discountPolicyPersistenceManager).persist(any(DiscountPolicy.class));
        }

        @Test
        @DisplayName("사용 횟수 제한 수정 성공")
        void shouldUpdateUsageLimitSuccessfully() {
            // Given
            DiscountPolicy existingPolicy = createExistingPolicy();
            UpdateDiscountPolicyCommand command = createUsageLimitUpdateCommand();

            when(discountPolicyReadManager.findById(eq(TEST_POLICY_ID))).thenReturn(existingPolicy);
            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            updateDiscountPolicyService.execute(command);

            // Then
            verify(discountPolicyReadManager).findById(TEST_POLICY_ID);
            verify(discountPolicyPersistenceManager).persist(any(DiscountPolicy.class));
        }

        @Test
        @DisplayName("비용 분담 비율 수정 성공")
        void shouldUpdateCostShareSuccessfully() {
            // Given
            DiscountPolicy existingPolicy = createExistingPolicy();
            UpdateDiscountPolicyCommand command = createCostShareUpdateCommand();

            when(discountPolicyReadManager.findById(eq(TEST_POLICY_ID))).thenReturn(existingPolicy);
            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            updateDiscountPolicyService.execute(command);

            // Then
            verify(discountPolicyReadManager).findById(TEST_POLICY_ID);
            verify(discountPolicyPersistenceManager).persist(any(DiscountPolicy.class));
        }

        @Test
        @DisplayName("우선순위 수정 성공")
        void shouldUpdatePrioritySuccessfully() {
            // Given
            DiscountPolicy existingPolicy = createExistingPolicy();
            UpdateDiscountPolicyCommand command = createPriorityUpdateCommand();

            when(discountPolicyReadManager.findById(eq(TEST_POLICY_ID))).thenReturn(existingPolicy);
            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            updateDiscountPolicyService.execute(command);

            // Then
            verify(discountPolicyReadManager).findById(TEST_POLICY_ID);
            verify(discountPolicyPersistenceManager).persist(any(DiscountPolicy.class));
        }

        @Test
        @DisplayName("여러 필드 동시 수정 성공")
        void shouldUpdateMultipleFieldsSuccessfully() {
            // Given
            DiscountPolicy existingPolicy = createExistingPolicy();
            UpdateDiscountPolicyCommand command = createMultipleFieldsUpdateCommand();

            when(discountPolicyReadManager.findById(eq(TEST_POLICY_ID))).thenReturn(existingPolicy);
            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            updateDiscountPolicyService.execute(command);

            // Then
            verify(discountPolicyReadManager).findById(TEST_POLICY_ID);
            verify(discountPolicyPersistenceManager).persist(any(DiscountPolicy.class));
        }
    }

    // ========== Helper Methods ==========

    private DiscountPolicy createExistingPolicy() {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(TEST_POLICY_ID),
                TEST_SELLER_ID,
                PolicyName.of("기존 할인"),
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

    private UpdateDiscountPolicyCommand createPolicyNameUpdateCommand() {
        return new UpdateDiscountPolicyCommand(
                TEST_POLICY_ID,
                TEST_SELLER_ID,
                "수정된 정책명",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    private UpdateDiscountPolicyCommand createMaximumDiscountAmountUpdateCommand() {
        return new UpdateDiscountPolicyCommand(
                TEST_POLICY_ID,
                TEST_SELLER_ID,
                null,
                20000L,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    private UpdateDiscountPolicyCommand createValidPeriodExtensionCommand() {
        return new UpdateDiscountPolicyCommand(
                TEST_POLICY_ID,
                TEST_SELLER_ID,
                null,
                null,
                null,
                Instant.parse("2026-06-30T23:59:59Z"),
                null,
                null,
                null,
                null,
                null);
    }

    private UpdateDiscountPolicyCommand createUsageLimitUpdateCommand() {
        return new UpdateDiscountPolicyCommand(
                TEST_POLICY_ID, TEST_SELLER_ID, null, null, null, null, 5, 5000, null, null, null);
    }

    private UpdateDiscountPolicyCommand createCostShareUpdateCommand() {
        return new UpdateDiscountPolicyCommand(
                TEST_POLICY_ID,
                TEST_SELLER_ID,
                null,
                null,
                null,
                null,
                null,
                null,
                new BigDecimal("70"),
                new BigDecimal("30"),
                null);
    }

    private UpdateDiscountPolicyCommand createPriorityUpdateCommand() {
        return new UpdateDiscountPolicyCommand(
                TEST_POLICY_ID, TEST_SELLER_ID, null, null, null, null, null, null, null, null, 50);
    }

    private UpdateDiscountPolicyCommand createMultipleFieldsUpdateCommand() {
        return new UpdateDiscountPolicyCommand(
                TEST_POLICY_ID,
                TEST_SELLER_ID,
                "종합 수정 정책",
                25000L,
                50000L,
                Instant.parse("2026-12-31T23:59:59Z"),
                3,
                3000,
                new BigDecimal("60"),
                new BigDecimal("40"),
                75);
    }
}
