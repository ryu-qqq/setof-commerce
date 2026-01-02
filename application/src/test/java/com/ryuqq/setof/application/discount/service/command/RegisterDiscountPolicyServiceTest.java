package com.ryuqq.setof.application.discount.service.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.discount.dto.command.RegisterDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.factory.command.DiscountPolicyCommandFactory;
import com.ryuqq.setof.application.discount.manager.command.DiscountPolicyPersistenceManager;
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
 * RegisterDiscountPolicyService 테스트
 *
 * <p>할인 정책 등록 서비스에 대한 단위 테스트
 */
@DisplayName("RegisterDiscountPolicyService")
@ExtendWith(MockitoExtension.class)
class RegisterDiscountPolicyServiceTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Instant FUTURE_TIME = Instant.parse("2025-12-31T23:59:59Z");
    private static final Long TEST_SELLER_ID = 1L;

    @Mock private DiscountPolicyCommandFactory discountPolicyCommandFactory;
    @Mock private DiscountPolicyPersistenceManager discountPolicyPersistenceManager;

    private RegisterDiscountPolicyService registerDiscountPolicyService;

    @BeforeEach
    void setUp() {
        registerDiscountPolicyService =
                new RegisterDiscountPolicyService(
                        discountPolicyCommandFactory, discountPolicyPersistenceManager);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("할인 정책 등록 성공")
        void shouldRegisterDiscountPolicySuccessfully() {
            // Given
            RegisterDiscountPolicyCommand command = createCommand();
            DiscountPolicy discountPolicy = createDiscountPolicy();
            DiscountPolicyId savedId = DiscountPolicyId.of(1L);

            when(discountPolicyCommandFactory.create(any())).thenReturn(discountPolicy);
            when(discountPolicyPersistenceManager.persist(any())).thenReturn(savedId);

            // When
            Long result = registerDiscountPolicyService.execute(command);

            // Then
            assertNotNull(result);
            assertEquals(1L, result);
            verify(discountPolicyCommandFactory).create(command);
            verify(discountPolicyPersistenceManager).persist(discountPolicy);
        }

        @Test
        @DisplayName("Factory와 Manager가 순서대로 호출된다")
        void shouldCallFactoryAndManagerInOrder() {
            // Given
            RegisterDiscountPolicyCommand command = createCommand();
            DiscountPolicy discountPolicy = createDiscountPolicy();
            DiscountPolicyId savedId = DiscountPolicyId.of(100L);

            when(discountPolicyCommandFactory.create(any())).thenReturn(discountPolicy);
            when(discountPolicyPersistenceManager.persist(any())).thenReturn(savedId);

            // When
            Long result = registerDiscountPolicyService.execute(command);

            // Then
            assertEquals(100L, result);
            verify(discountPolicyCommandFactory).create(command);
            verify(discountPolicyPersistenceManager).persist(discountPolicy);
        }
    }

    // ========== Helper Methods ==========

    private RegisterDiscountPolicyCommand createCommand() {
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

    private DiscountPolicy createDiscountPolicy() {
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
}
