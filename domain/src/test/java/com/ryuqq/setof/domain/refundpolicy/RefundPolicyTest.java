package com.ryuqq.setof.domain.refundpolicy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.refundpolicy.aggregate.RefundPolicy;
import com.ryuqq.setof.domain.refundpolicy.vo.PolicyName;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundDeliveryCost;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundGuide;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPeriodDays;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPolicyId;
import com.ryuqq.setof.domain.refundpolicy.vo.ReturnAddress;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * RefundPolicy Aggregate 테스트
 *
 * <p>반품/교환 정책 관리에 대한 도메인 로직을 테스트합니다.
 */
@DisplayName("RefundPolicy Aggregate")
class RefundPolicyTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Long TEST_SELLER_ID = 1L;

    @Nested
    @DisplayName("create() - 신규 반품 정책 생성")
    class Create {

        @Test
        @DisplayName("신규 반품 정책을 생성할 수 있다")
        void shouldCreateNewRefundPolicy() {
            // given
            PolicyName policyName = PolicyName.of("기본 환불");
            ReturnAddress returnAddress = ReturnAddress.of("서울시 강남구", "테헤란로 123", "06234");
            RefundPeriodDays periodDays = RefundPeriodDays.of(7);
            RefundDeliveryCost deliveryCost = RefundDeliveryCost.of(3000);
            RefundGuide guide = RefundGuide.of("상품 수령 후 7일 이내 환불 가능");

            // when
            RefundPolicy policy =
                    RefundPolicy.create(
                            TEST_SELLER_ID,
                            policyName,
                            returnAddress,
                            periodDays,
                            deliveryCost,
                            guide,
                            true,
                            1,
                            FIXED_TIME);

            // then
            assertNotNull(policy);
            assertNull(policy.getId());
            assertEquals(TEST_SELLER_ID, policy.getSellerId());
            assertEquals("기본 환불", policy.getPolicyNameValue());
            assertEquals("서울시 강남구", policy.getReturnAddressLine1());
            assertEquals("테헤란로 123", policy.getReturnAddressLine2());
            assertEquals("06234", policy.getReturnZipCode());
            assertEquals(7, policy.getRefundPeriodDaysValue());
            assertEquals(3000, policy.getRefundDeliveryCostValue());
            assertEquals("상품 수령 후 7일 이내 환불 가능", policy.getRefundGuideValue());
            assertTrue(policy.isDefault());
            assertEquals(1, policy.getDisplayOrder());
            assertFalse(policy.isDeleted());
        }

        @Test
        @DisplayName("반품 안내 없이 정책을 생성할 수 있다")
        void shouldCreatePolicyWithoutRefundGuide() {
            // given
            PolicyName policyName = PolicyName.of("심플 환불");
            ReturnAddress returnAddress = ReturnAddress.of("서울시 강남구", null, "06234");
            RefundPeriodDays periodDays = RefundPeriodDays.of(14);
            RefundDeliveryCost deliveryCost = RefundDeliveryCost.of(0);

            // when
            RefundPolicy policy =
                    RefundPolicy.create(
                            TEST_SELLER_ID,
                            policyName,
                            returnAddress,
                            periodDays,
                            deliveryCost,
                            null,
                            false,
                            1,
                            FIXED_TIME);

            // then
            assertNotNull(policy);
            assertNull(policy.getRefundGuideValue());
            assertNull(policy.getReturnAddressLine2());
            assertFalse(policy.isDefault());
        }
    }

    @Nested
    @DisplayName("reconstitute() - Persistence 복원")
    class Reconstitute {

        @Test
        @DisplayName("Persistence에서 모든 필드를 복원할 수 있다")
        void shouldReconstitutePolicyFromPersistence() {
            // given
            RefundPolicyId id = RefundPolicyId.of(1L);
            PolicyName policyName = PolicyName.of("복원된 정책");
            ReturnAddress returnAddress = ReturnAddress.of("서울시 강남구", "테헤란로 123", "06234");
            RefundPeriodDays periodDays = RefundPeriodDays.of(7);
            RefundDeliveryCost deliveryCost = RefundDeliveryCost.of(3000);
            RefundGuide guide = RefundGuide.of("환불 안내");
            Instant createdAt = Instant.parse("2024-01-01T00:00:00Z");
            Instant updatedAt = Instant.parse("2024-06-01T00:00:00Z");

            // when
            RefundPolicy policy =
                    RefundPolicy.reconstitute(
                            id,
                            TEST_SELLER_ID,
                            policyName,
                            returnAddress,
                            periodDays,
                            deliveryCost,
                            guide,
                            true,
                            1,
                            createdAt,
                            updatedAt,
                            null);

            // then
            assertEquals(1L, policy.getIdValue());
            assertEquals("복원된 정책", policy.getPolicyNameValue());
            assertTrue(policy.isDefault());
            assertFalse(policy.isDeleted());
            assertEquals(createdAt, policy.getCreatedAt());
            assertEquals(updatedAt, policy.getUpdatedAt());
        }

        @Test
        @DisplayName("삭제된 정책을 복원할 수 있다")
        void shouldReconstituteDeletedPolicy() {
            // given
            Instant deletedAt = Instant.parse("2024-12-01T00:00:00Z");

            // when
            RefundPolicy policy =
                    RefundPolicy.reconstitute(
                            RefundPolicyId.of(1L),
                            TEST_SELLER_ID,
                            PolicyName.of("삭제된 정책"),
                            ReturnAddress.of("서울시 강남구", null, "06234"),
                            RefundPeriodDays.of(7),
                            RefundDeliveryCost.of(3000),
                            null,
                            false,
                            1,
                            FIXED_TIME,
                            FIXED_TIME,
                            deletedAt);

            // then
            assertTrue(policy.isDeleted());
            assertEquals(deletedAt, policy.getDeletedAt());
        }
    }

    @Nested
    @DisplayName("update() - 반품 정책 수정")
    class Update {

        @Test
        @DisplayName("반품 정책 정보를 수정할 수 있다")
        void shouldUpdatePolicy() {
            // given
            RefundPolicy policy = createDefaultPolicy();
            PolicyName newName = PolicyName.of("수정된 정책");
            ReturnAddress newAddress = ReturnAddress.of("서울시 서초구", "서초대로 456", "06500");
            RefundPeriodDays newPeriod = RefundPeriodDays.of(14);
            RefundDeliveryCost newCost = RefundDeliveryCost.of(5000);
            RefundGuide newGuide = RefundGuide.of("새로운 환불 안내");

            // when
            RefundPolicy updatedPolicy =
                    policy.update(newName, newAddress, newPeriod, newCost, newGuide, FIXED_TIME);

            // then
            assertEquals("수정된 정책", updatedPolicy.getPolicyNameValue());
            assertEquals("서울시 서초구", updatedPolicy.getReturnAddressLine1());
            assertEquals("서초대로 456", updatedPolicy.getReturnAddressLine2());
            assertEquals(14, updatedPolicy.getRefundPeriodDaysValue());
            assertEquals(5000, updatedPolicy.getRefundDeliveryCostValue());
            assertEquals("새로운 환불 안내", updatedPolicy.getRefundGuideValue());
            // 기본 정책 여부 및 표시 순서는 유지
            assertEquals(policy.isDefault(), updatedPolicy.isDefault());
            assertEquals(policy.getDisplayOrder(), updatedPolicy.getDisplayOrder());
        }
    }

    @Nested
    @DisplayName("setAsDefault() / unsetDefault() - 기본 정책 설정")
    class DefaultSetting {

        @Test
        @DisplayName("기본 정책으로 설정할 수 있다")
        void shouldSetAsDefault() {
            // given
            RefundPolicy policy = createNonDefaultPolicy();
            assertFalse(policy.isDefault());

            // when
            RefundPolicy defaultPolicy = policy.setAsDefault(FIXED_TIME);

            // then
            assertTrue(defaultPolicy.isDefault());
        }

        @Test
        @DisplayName("기본 정책을 해제할 수 있다")
        void shouldUnsetDefault() {
            // given
            RefundPolicy policy = createDefaultPolicy();
            assertTrue(policy.isDefault());

            // when
            RefundPolicy nonDefaultPolicy = policy.unsetDefault(FIXED_TIME);

            // then
            assertFalse(nonDefaultPolicy.isDefault());
        }
    }

    @Nested
    @DisplayName("delete() - 반품 정책 삭제")
    class Delete {

        @Test
        @DisplayName("반품 정책을 소프트 삭제할 수 있다")
        void shouldSoftDeletePolicy() {
            // given
            RefundPolicy policy = createDefaultPolicy();
            assertFalse(policy.isDeleted());
            assertTrue(policy.isDefault());

            // when
            RefundPolicy deletedPolicy = policy.delete(FIXED_TIME);

            // then
            assertTrue(deletedPolicy.isDeleted());
            assertFalse(deletedPolicy.isDefault()); // 삭제 시 기본 정책 해제
            assertNotNull(deletedPolicy.getDeletedAt());
        }
    }

    @Nested
    @DisplayName("isWithinRefundPeriod() - 반품 가능 기간 확인")
    class RefundPeriodCheck {

        @Test
        @DisplayName("반품 가능 기간 내이면 true를 반환한다")
        void shouldReturnTrueWhenWithinPeriod() {
            // given
            RefundPolicy policy = createDefaultPolicy(); // 7일 환불 기간
            Instant orderDate = FIXED_TIME;
            Instant currentDate = FIXED_TIME.plus(6, ChronoUnit.DAYS); // 6일 후

            // when
            boolean result = policy.isWithinRefundPeriod(orderDate, currentDate);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("반품 가능 기간을 초과하면 false를 반환한다")
        void shouldReturnFalseWhenExceedsPeriod() {
            // given
            RefundPolicy policy = createDefaultPolicy(); // 7일 환불 기간
            Instant orderDate = FIXED_TIME;
            Instant currentDate = FIXED_TIME.plus(8, ChronoUnit.DAYS); // 8일 후

            // when
            boolean result = policy.isWithinRefundPeriod(orderDate, currentDate);

            // then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("Law of Demeter Helper Methods")
    class HelperMethods {

        @Test
        @DisplayName("getIdValue()는 ID가 없으면 null을 반환한다")
        void shouldReturnNullWhenIdIsNull() {
            // given
            RefundPolicy policy =
                    RefundPolicy.create(
                            TEST_SELLER_ID,
                            PolicyName.of("테스트"),
                            ReturnAddress.of("서울시 강남구", null, "06234"),
                            RefundPeriodDays.of(7),
                            RefundDeliveryCost.of(3000),
                            null,
                            true,
                            1,
                            FIXED_TIME);

            // then
            assertNull(policy.getIdValue());
            assertFalse(policy.hasId());
        }
    }

    // ========== Helper Methods ==========

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
