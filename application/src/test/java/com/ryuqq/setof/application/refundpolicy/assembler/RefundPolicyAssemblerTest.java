package com.ryuqq.setof.application.refundpolicy.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.application.refundpolicy.dto.command.RegisterRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyResponse;
import com.ryuqq.setof.domain.refundpolicy.aggregate.RefundPolicy;
import com.ryuqq.setof.domain.refundpolicy.vo.PolicyName;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundDeliveryCost;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundGuide;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPeriodDays;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPolicyId;
import com.ryuqq.setof.domain.refundpolicy.vo.ReturnAddress;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * RefundPolicyAssembler 테스트
 *
 * <p>Command ↔ Domain ↔ Response 변환에 대한 단위 테스트
 */
@DisplayName("RefundPolicyAssembler")
class RefundPolicyAssemblerTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Long TEST_SELLER_ID = 1L;

    private RefundPolicyAssembler refundPolicyAssembler;

    @BeforeEach
    void setUp() {
        refundPolicyAssembler = new RefundPolicyAssembler();
    }

    @Nested
    @DisplayName("toDomain")
    class ToDomainTest {

        @Test
        @DisplayName("모든 필드가 있는 Command를 Domain으로 변환")
        void shouldConvertFullCommandToDomain() {
            // Given
            RegisterRefundPolicyCommand command = createFullCommand();

            // When
            RefundPolicy refundPolicy = refundPolicyAssembler.toDomain(command, FIXED_TIME);

            // Then
            assertNotNull(refundPolicy);
            assertEquals("기본 환불", refundPolicy.getPolicyNameValue());
            assertEquals("서울시 강남구", refundPolicy.getReturnAddressLine1());
            assertEquals("테헤란로 123", refundPolicy.getReturnAddressLine2());
            assertEquals("06234", refundPolicy.getReturnZipCode());
            assertEquals(7, refundPolicy.getRefundPeriodDaysValue());
            assertEquals(3000, refundPolicy.getRefundDeliveryCostValue());
            assertEquals("상품 수령 후 7일 이내 환불 가능", refundPolicy.getRefundGuideValue());
            assertTrue(refundPolicy.isDefault());
            assertEquals(1, refundPolicy.getDisplayOrder());
            assertFalse(refundPolicy.isDeleted());
        }

        @Test
        @DisplayName("선택 필드가 없는 Command를 Domain으로 변환")
        void shouldConvertMinimalCommandToDomain() {
            // Given
            RegisterRefundPolicyCommand command = createMinimalCommand();

            // When
            RefundPolicy refundPolicy = refundPolicyAssembler.toDomain(command, FIXED_TIME);

            // Then
            assertNotNull(refundPolicy);
            assertEquals("심플 환불", refundPolicy.getPolicyNameValue());
            assertEquals(14, refundPolicy.getRefundPeriodDaysValue());
            assertNull(refundPolicy.getReturnAddressLine2());
            assertNull(refundPolicy.getRefundGuideValue());
            assertFalse(refundPolicy.isDefault());
        }
    }

    @Nested
    @DisplayName("toResponse")
    class ToResponseTest {

        @Test
        @DisplayName("Domain을 Response로 변환")
        void shouldConvertDomainToResponse() {
            // Given
            RefundPolicy refundPolicy = createDefaultPolicy();

            // When
            RefundPolicyResponse response = refundPolicyAssembler.toResponse(refundPolicy);

            // Then
            assertNotNull(response);
            assertEquals(1L, response.refundPolicyId());
            assertEquals(TEST_SELLER_ID, response.sellerId());
            assertEquals("기본 환불", response.policyName());
            assertEquals("서울시 강남구", response.returnAddressLine1());
            assertEquals("테헤란로 123", response.returnAddressLine2());
            assertEquals("06234", response.returnZipCode());
            assertEquals(7, response.refundPeriodDays());
            assertEquals(3000, response.refundDeliveryCost());
            assertEquals("환불 안내", response.refundGuide());
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
            List<RefundPolicy> policies = List.of(createDefaultPolicy(), createNonDefaultPolicy());

            // When
            List<RefundPolicyResponse> responses = refundPolicyAssembler.toResponses(policies);

            // Then
            assertNotNull(responses);
            assertEquals(2, responses.size());
            assertEquals(1L, responses.get(0).refundPolicyId());
            assertEquals(2L, responses.get(1).refundPolicyId());
        }
    }

    // ========== Helper Methods ==========

    private RegisterRefundPolicyCommand createFullCommand() {
        return RegisterRefundPolicyCommand.of(
                TEST_SELLER_ID,
                "기본 환불",
                "서울시 강남구",
                "테헤란로 123",
                "06234",
                7,
                3000,
                "상품 수령 후 7일 이내 환불 가능",
                true,
                1);
    }

    private RegisterRefundPolicyCommand createMinimalCommand() {
        return RegisterRefundPolicyCommand.of(
                TEST_SELLER_ID, "심플 환불", "서울시 강남구", null, "06234", 14, 0, null, false, 2);
    }

    private RefundPolicy createDefaultPolicy() {
        return RefundPolicy.reconstitute(
                RefundPolicyId.of(1L),
                TEST_SELLER_ID,
                PolicyName.of("기본 환불"),
                ReturnAddress.of("서울시 강남구", "테헤란로 123", "06234"),
                RefundPeriodDays.of(7),
                RefundDeliveryCost.of(3000),
                RefundGuide.of("환불 안내"),
                true,
                1,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    private RefundPolicy createNonDefaultPolicy() {
        return RefundPolicy.reconstitute(
                RefundPolicyId.of(2L),
                TEST_SELLER_ID,
                PolicyName.of("추가 환불"),
                ReturnAddress.of("서울시 서초구", null, "06500"),
                RefundPeriodDays.of(14),
                RefundDeliveryCost.of(5000),
                null,
                false,
                2,
                FIXED_TIME,
                FIXED_TIME,
                null);
    }
}
