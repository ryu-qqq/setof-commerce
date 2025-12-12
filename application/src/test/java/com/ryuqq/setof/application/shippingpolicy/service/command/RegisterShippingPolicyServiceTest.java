package com.ryuqq.setof.application.shippingpolicy.service.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.shippingpolicy.dto.command.RegisterShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.factory.command.ShippingPolicyCommandFactory;
import com.ryuqq.setof.application.shippingpolicy.manager.command.ShippingPolicyPersistenceManager;
import com.ryuqq.setof.domain.shippingpolicy.aggregate.ShippingPolicy;
import com.ryuqq.setof.domain.shippingpolicy.vo.DeliveryCost;
import com.ryuqq.setof.domain.shippingpolicy.vo.DeliveryGuide;
import com.ryuqq.setof.domain.shippingpolicy.vo.DisplayOrder;
import com.ryuqq.setof.domain.shippingpolicy.vo.FreeShippingThreshold;
import com.ryuqq.setof.domain.shippingpolicy.vo.PolicyName;
import com.ryuqq.setof.domain.shippingpolicy.vo.ShippingPolicyId;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * RegisterShippingPolicyService 테스트
 *
 * <p>배송 정책 등록 서비스에 대한 단위 테스트
 */
@DisplayName("RegisterShippingPolicyService")
@ExtendWith(MockitoExtension.class)
class RegisterShippingPolicyServiceTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Long TEST_SELLER_ID = 1L;

    @Mock private ShippingPolicyCommandFactory shippingPolicyCommandFactory;
    @Mock private ShippingPolicyPersistenceManager shippingPolicyPersistenceManager;

    private RegisterShippingPolicyService registerShippingPolicyService;

    @BeforeEach
    void setUp() {
        registerShippingPolicyService =
                new RegisterShippingPolicyService(
                        shippingPolicyCommandFactory, shippingPolicyPersistenceManager);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("배송 정책 등록 성공")
        void shouldRegisterShippingPolicySuccessfully() {
            // Given
            RegisterShippingPolicyCommand command = createCommand();
            ShippingPolicy shippingPolicy = createShippingPolicy();
            ShippingPolicyId savedId = ShippingPolicyId.of(1L);

            when(shippingPolicyCommandFactory.create(any())).thenReturn(shippingPolicy);
            when(shippingPolicyPersistenceManager.persist(any())).thenReturn(savedId);

            // When
            Long result = registerShippingPolicyService.execute(command);

            // Then
            assertNotNull(result);
            assertEquals(1L, result);
            verify(shippingPolicyCommandFactory).create(command);
            verify(shippingPolicyPersistenceManager).persist(shippingPolicy);
        }

        @Test
        @DisplayName("Factory와 Manager가 순서대로 호출된다")
        void shouldCallFactoryAndManagerInOrder() {
            // Given
            RegisterShippingPolicyCommand command = createCommand();
            ShippingPolicy shippingPolicy = createShippingPolicy();
            ShippingPolicyId savedId = ShippingPolicyId.of(100L);

            when(shippingPolicyCommandFactory.create(any())).thenReturn(shippingPolicy);
            when(shippingPolicyPersistenceManager.persist(any())).thenReturn(savedId);

            // When
            Long result = registerShippingPolicyService.execute(command);

            // Then
            assertEquals(100L, result);
            verify(shippingPolicyCommandFactory).create(command);
            verify(shippingPolicyPersistenceManager).persist(shippingPolicy);
        }
    }

    // ========== Helper Methods ==========

    private RegisterShippingPolicyCommand createCommand() {
        return RegisterShippingPolicyCommand.of(
                TEST_SELLER_ID, "기본 배송", 3000, 50000, "주문 후 1-3일 이내 배송", true, 1);
    }

    private ShippingPolicy createShippingPolicy() {
        return ShippingPolicy.reconstitute(
                ShippingPolicyId.of(1L),
                TEST_SELLER_ID,
                PolicyName.of("기본 배송"),
                DeliveryCost.of(3000),
                FreeShippingThreshold.of(50000),
                DeliveryGuide.of("주문 후 1-3일 이내 배송"),
                true,
                DisplayOrder.of(1),
                FIXED_TIME,
                FIXED_TIME,
                null);
    }
}
