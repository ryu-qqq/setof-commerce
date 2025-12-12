package com.ryuqq.setof.application.shippingpolicy.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.application.shippingpolicy.dto.command.RegisterShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyResponse;
import com.ryuqq.setof.domain.shippingpolicy.aggregate.ShippingPolicy;
import com.ryuqq.setof.domain.shippingpolicy.vo.DeliveryCost;
import com.ryuqq.setof.domain.shippingpolicy.vo.DeliveryGuide;
import com.ryuqq.setof.domain.shippingpolicy.vo.DisplayOrder;
import com.ryuqq.setof.domain.shippingpolicy.vo.FreeShippingThreshold;
import com.ryuqq.setof.domain.shippingpolicy.vo.PolicyName;
import com.ryuqq.setof.domain.shippingpolicy.vo.ShippingPolicyId;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * ShippingPolicyAssembler 테스트
 *
 * <p>Command ↔ Domain ↔ Response 변환에 대한 단위 테스트
 */
@DisplayName("ShippingPolicyAssembler")
class ShippingPolicyAssemblerTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Long TEST_SELLER_ID = 1L;

    private ShippingPolicyAssembler shippingPolicyAssembler;

    @BeforeEach
    void setUp() {
        shippingPolicyAssembler = new ShippingPolicyAssembler();
    }

    @Nested
    @DisplayName("toDomain")
    class ToDomainTest {

        @Test
        @DisplayName("모든 필드가 있는 Command를 Domain으로 변환")
        void shouldConvertFullCommandToDomain() {
            // Given
            RegisterShippingPolicyCommand command = createFullCommand();

            // When
            ShippingPolicy shippingPolicy = shippingPolicyAssembler.toDomain(command, FIXED_TIME);

            // Then
            assertNotNull(shippingPolicy);
            assertEquals("기본 배송", shippingPolicy.getPolicyNameValue());
            assertEquals(3000, shippingPolicy.getDefaultDeliveryCostValue());
            assertEquals(50000, shippingPolicy.getFreeShippingThresholdValue());
            assertEquals("주문 후 1-3일 이내 배송", shippingPolicy.getDeliveryGuideValue());
            assertTrue(shippingPolicy.isDefault());
            assertEquals(1, shippingPolicy.getDisplayOrderValue());
            assertFalse(shippingPolicy.isDeleted());
        }

        @Test
        @DisplayName("선택 필드가 없는 Command를 Domain으로 변환")
        void shouldConvertMinimalCommandToDomain() {
            // Given
            RegisterShippingPolicyCommand command = createMinimalCommand();

            // When
            ShippingPolicy shippingPolicy = shippingPolicyAssembler.toDomain(command, FIXED_TIME);

            // Then
            assertNotNull(shippingPolicy);
            assertEquals("유료 배송", shippingPolicy.getPolicyNameValue());
            assertEquals(5000, shippingPolicy.getDefaultDeliveryCostValue());
            assertNull(shippingPolicy.getFreeShippingThresholdValue());
            assertNull(shippingPolicy.getDeliveryGuideValue());
            assertFalse(shippingPolicy.isDefault());
        }
    }

    @Nested
    @DisplayName("toResponse")
    class ToResponseTest {

        @Test
        @DisplayName("Domain을 Response로 변환")
        void shouldConvertDomainToResponse() {
            // Given
            ShippingPolicy shippingPolicy = createDefaultPolicy();

            // When
            ShippingPolicyResponse response = shippingPolicyAssembler.toResponse(shippingPolicy);

            // Then
            assertNotNull(response);
            assertEquals(1L, response.shippingPolicyId());
            assertEquals(TEST_SELLER_ID, response.sellerId());
            assertEquals("기본 배송", response.policyName());
            assertEquals(3000, response.defaultDeliveryCost());
            assertEquals(50000, response.freeShippingThreshold());
            assertEquals("주문 후 1-3일 이내 배송", response.deliveryGuide());
            assertTrue(response.isDefault());
            assertEquals(1, response.displayOrder());
        }
    }

    @Nested
    @DisplayName("toResponses")
    class ToResponsesTest {

        @Test
        @DisplayName("Domain 목록을 Response 목록으로 변환")
        void shouldConvertDomainListToResponseList() {
            // Given
            List<ShippingPolicy> policies =
                    List.of(createDefaultPolicy(), createNonDefaultPolicy());

            // When
            List<ShippingPolicyResponse> responses = shippingPolicyAssembler.toResponses(policies);

            // Then
            assertNotNull(responses);
            assertEquals(2, responses.size());
            assertEquals(1L, responses.get(0).shippingPolicyId());
            assertEquals(2L, responses.get(1).shippingPolicyId());
        }
    }

    // ========== Helper Methods ==========

    private RegisterShippingPolicyCommand createFullCommand() {
        return new RegisterShippingPolicyCommand(
                TEST_SELLER_ID, "기본 배송", 3000, 50000, "주문 후 1-3일 이내 배송", true, 1);
    }

    private RegisterShippingPolicyCommand createMinimalCommand() {
        return new RegisterShippingPolicyCommand(
                TEST_SELLER_ID, "유료 배송", 5000, null, null, false, 2);
    }

    private ShippingPolicy createDefaultPolicy() {
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

    private ShippingPolicy createNonDefaultPolicy() {
        return ShippingPolicy.reconstitute(
                ShippingPolicyId.of(2L),
                TEST_SELLER_ID,
                PolicyName.of("추가 배송"),
                DeliveryCost.of(5000),
                FreeShippingThreshold.of(100000),
                null,
                false,
                DisplayOrder.of(2),
                FIXED_TIME,
                FIXED_TIME,
                null);
    }
}
