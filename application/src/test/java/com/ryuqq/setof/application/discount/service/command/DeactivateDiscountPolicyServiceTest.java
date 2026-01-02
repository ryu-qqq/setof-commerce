package com.ryuqq.setof.application.discount.service.command;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.discount.dto.command.DeactivateDiscountPolicyCommand;
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
 * DeactivateDiscountPolicyService 테스트
 *
 * <p>할인 정책 비활성화 서비스에 대한 단위 테스트
 */
@DisplayName("DeactivateDiscountPolicyService")
@ExtendWith(MockitoExtension.class)
class DeactivateDiscountPolicyServiceTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Instant FUTURE_TIME = Instant.parse("2025-12-31T23:59:59Z");
    private static final Clock FIXED_CLOCK = Clock.fixed(FIXED_TIME, ZoneId.of("UTC"));
    private static final Long TEST_SELLER_ID = 1L;
    private static final Long TEST_POLICY_ID = 1L;

    @Mock private DiscountPolicyReadManager discountPolicyReadManager;
    @Mock private DiscountPolicyPersistenceManager discountPolicyPersistenceManager;
    @Mock private ClockHolder clockHolder;

    private DeactivateDiscountPolicyService deactivateDiscountPolicyService;

    @BeforeEach
    void setUp() {
        deactivateDiscountPolicyService =
                new DeactivateDiscountPolicyService(
                        discountPolicyReadManager, discountPolicyPersistenceManager, clockHolder);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("활성화된 정책 비활성화 성공")
        void shouldDeactivateActivePolicySuccessfully() {
            // Given
            DiscountPolicy activePolicy = createActivePolicy();
            DeactivateDiscountPolicyCommand command =
                    new DeactivateDiscountPolicyCommand(TEST_POLICY_ID, TEST_SELLER_ID);

            when(discountPolicyReadManager.findById(eq(TEST_POLICY_ID))).thenReturn(activePolicy);
            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            deactivateDiscountPolicyService.execute(command);

            // Then
            verify(discountPolicyReadManager).findById(TEST_POLICY_ID);
            verify(discountPolicyPersistenceManager).persist(any(DiscountPolicy.class));
        }

        @Test
        @DisplayName("이미 비활성화된 정책에도 비활성화 호출 가능")
        void shouldHandleAlreadyInactivePolicy() {
            // Given
            DiscountPolicy inactivePolicy = createInactivePolicy();
            DeactivateDiscountPolicyCommand command =
                    new DeactivateDiscountPolicyCommand(TEST_POLICY_ID, TEST_SELLER_ID);

            when(discountPolicyReadManager.findById(eq(TEST_POLICY_ID))).thenReturn(inactivePolicy);
            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            deactivateDiscountPolicyService.execute(command);

            // Then
            verify(discountPolicyReadManager).findById(TEST_POLICY_ID);
            verify(discountPolicyPersistenceManager).persist(any(DiscountPolicy.class));
        }
    }

    // ========== Helper Methods ==========

    private DiscountPolicy createActivePolicy() {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(TEST_POLICY_ID),
                TEST_SELLER_ID,
                PolicyName.of("활성 정책"),
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
                true, // active
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private DiscountPolicy createInactivePolicy() {
        return DiscountPolicy.reconstitute(
                DiscountPolicyId.of(TEST_POLICY_ID),
                TEST_SELLER_ID,
                PolicyName.of("비활성 정책"),
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
                false, // inactive
                FIXED_TIME,
                FIXED_TIME,
                null);
    }
}
