package com.ryuqq.setof.domain.shippingpolicy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.shippingpolicy.aggregate.ShippingPolicy;
import com.ryuqq.setof.domain.shippingpolicy.vo.DeliveryCost;
import com.ryuqq.setof.domain.shippingpolicy.vo.DeliveryGuide;
import com.ryuqq.setof.domain.shippingpolicy.vo.DisplayOrder;
import com.ryuqq.setof.domain.shippingpolicy.vo.FreeShippingThreshold;
import com.ryuqq.setof.domain.shippingpolicy.vo.PolicyName;
import com.ryuqq.setof.domain.shippingpolicy.vo.ShippingPolicyId;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * ShippingPolicy Aggregate 테스트
 *
 * <p>배송 정책 관리에 대한 도메인 로직을 테스트합니다.
 */
@DisplayName("ShippingPolicy Aggregate")
class ShippingPolicyTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Long TEST_SELLER_ID = 1L;

    @Nested
    @DisplayName("create() - 신규 배송 정책 생성")
    class Create {

        @Test
        @DisplayName("신규 배송 정책을 생성할 수 있다")
        void shouldCreateNewShippingPolicy() {
            // given
            PolicyName policyName = PolicyName.of("기본 배송");
            DeliveryCost deliveryCost = DeliveryCost.of(3000);
            FreeShippingThreshold freeThreshold = FreeShippingThreshold.of(50000);
            DeliveryGuide guide = DeliveryGuide.of("주문 후 1-3일 이내 배송");
            DisplayOrder displayOrder = DisplayOrder.of(1);

            // when
            ShippingPolicy policy =
                    ShippingPolicy.create(
                            TEST_SELLER_ID,
                            policyName,
                            deliveryCost,
                            freeThreshold,
                            guide,
                            true,
                            displayOrder,
                            FIXED_TIME);

            // then
            assertNotNull(policy);
            assertNull(policy.getId());
            assertEquals(TEST_SELLER_ID, policy.getSellerId());
            assertEquals("기본 배송", policy.getPolicyNameValue());
            assertEquals(3000, policy.getDefaultDeliveryCostValue());
            assertEquals(50000, policy.getFreeShippingThresholdValue());
            assertEquals("주문 후 1-3일 이내 배송", policy.getDeliveryGuideValue());
            assertTrue(policy.isDefault());
            assertEquals(1, policy.getDisplayOrderValue());
            assertFalse(policy.isDeleted());
        }

        @Test
        @DisplayName("무료 배송 기준 금액 없이 정책을 생성할 수 있다")
        void shouldCreatePolicyWithoutFreeShippingThreshold() {
            // given
            PolicyName policyName = PolicyName.of("유료 배송");
            DeliveryCost deliveryCost = DeliveryCost.of(5000);
            DisplayOrder displayOrder = DisplayOrder.of(1);

            // when
            ShippingPolicy policy =
                    ShippingPolicy.create(
                            TEST_SELLER_ID,
                            policyName,
                            deliveryCost,
                            null,
                            null,
                            false,
                            displayOrder,
                            FIXED_TIME);

            // then
            assertNotNull(policy);
            assertNull(policy.getFreeShippingThresholdValue());
            assertNull(policy.getDeliveryGuideValue());
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
            ShippingPolicyId id = ShippingPolicyId.of(1L);
            PolicyName policyName = PolicyName.of("복원된 정책");
            DeliveryCost deliveryCost = DeliveryCost.of(3000);
            FreeShippingThreshold freeThreshold = FreeShippingThreshold.of(50000);
            DeliveryGuide guide = DeliveryGuide.of("배송 안내");
            DisplayOrder displayOrder = DisplayOrder.of(1);
            Instant createdAt = Instant.parse("2024-01-01T00:00:00Z");
            Instant updatedAt = Instant.parse("2024-06-01T00:00:00Z");

            // when
            ShippingPolicy policy =
                    ShippingPolicy.reconstitute(
                            id,
                            TEST_SELLER_ID,
                            policyName,
                            deliveryCost,
                            freeThreshold,
                            guide,
                            true,
                            displayOrder,
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
            ShippingPolicy policy =
                    ShippingPolicy.reconstitute(
                            ShippingPolicyId.of(1L),
                            TEST_SELLER_ID,
                            PolicyName.of("삭제된 정책"),
                            DeliveryCost.of(3000),
                            null,
                            null,
                            false,
                            DisplayOrder.of(1),
                            FIXED_TIME,
                            FIXED_TIME,
                            deletedAt);

            // then
            assertTrue(policy.isDeleted());
            assertEquals(deletedAt, policy.getDeletedAt());
        }
    }

    @Nested
    @DisplayName("update() - 배송 정책 수정")
    class Update {

        @Test
        @DisplayName("배송 정책 정보를 수정할 수 있다")
        void shouldUpdatePolicy() {
            // given
            ShippingPolicy policy = createDefaultPolicy();
            PolicyName newName = PolicyName.of("수정된 정책");
            DeliveryCost newCost = DeliveryCost.of(5000);
            FreeShippingThreshold newThreshold = FreeShippingThreshold.of(100000);
            DeliveryGuide newGuide = DeliveryGuide.of("새로운 배송 안내");
            DisplayOrder newOrder = DisplayOrder.of(2);

            // when
            ShippingPolicy updatedPolicy =
                    policy.update(newName, newCost, newThreshold, newGuide, newOrder, FIXED_TIME);

            // then
            assertEquals("수정된 정책", updatedPolicy.getPolicyNameValue());
            assertEquals(5000, updatedPolicy.getDefaultDeliveryCostValue());
            assertEquals(100000, updatedPolicy.getFreeShippingThresholdValue());
            assertEquals("새로운 배송 안내", updatedPolicy.getDeliveryGuideValue());
            assertEquals(2, updatedPolicy.getDisplayOrderValue());
            // 기본 정책 여부는 유지
            assertEquals(policy.isDefault(), updatedPolicy.isDefault());
        }
    }

    @Nested
    @DisplayName("setAsDefault() / unsetDefault() - 기본 정책 설정")
    class DefaultSetting {

        @Test
        @DisplayName("기본 정책으로 설정할 수 있다")
        void shouldSetAsDefault() {
            // given
            ShippingPolicy policy = createNonDefaultPolicy();
            assertFalse(policy.isDefault());

            // when
            ShippingPolicy defaultPolicy = policy.setAsDefault(FIXED_TIME);

            // then
            assertTrue(defaultPolicy.isDefault());
        }

        @Test
        @DisplayName("기본 정책을 해제할 수 있다")
        void shouldUnsetDefault() {
            // given
            ShippingPolicy policy = createDefaultPolicy();
            assertTrue(policy.isDefault());

            // when
            ShippingPolicy nonDefaultPolicy = policy.unsetDefault(FIXED_TIME);

            // then
            assertFalse(nonDefaultPolicy.isDefault());
        }
    }

    @Nested
    @DisplayName("delete() - 배송 정책 삭제")
    class Delete {

        @Test
        @DisplayName("배송 정책을 소프트 삭제할 수 있다")
        void shouldSoftDeletePolicy() {
            // given
            ShippingPolicy policy = createDefaultPolicy();
            assertFalse(policy.isDeleted());
            assertTrue(policy.isDefault());

            // when
            ShippingPolicy deletedPolicy = policy.delete(FIXED_TIME);

            // then
            assertTrue(deletedPolicy.isDeleted());
            assertFalse(deletedPolicy.isDefault()); // 삭제 시 기본 정책 해제
            assertNotNull(deletedPolicy.getDeletedAt());
        }
    }

    @Nested
    @DisplayName("calculateDeliveryCost() - 배송비 계산")
    class CalculateDeliveryCost {

        @Test
        @DisplayName("무료 배송 기준 금액 미만이면 배송비를 반환한다")
        void shouldReturnDeliveryCostWhenBelowThreshold() {
            // given
            ShippingPolicy policy = createDefaultPolicy(); // freeThreshold: 50000, cost: 3000

            // when
            int cost = policy.calculateDeliveryCost(30000);

            // then
            assertEquals(3000, cost);
        }

        @Test
        @DisplayName("무료 배송 기준 금액 이상이면 0원을 반환한다")
        void shouldReturnZeroWhenAboveThreshold() {
            // given
            ShippingPolicy policy = createDefaultPolicy(); // freeThreshold: 50000

            // when
            int cost = policy.calculateDeliveryCost(50000);

            // then
            assertEquals(0, cost);
        }

        @Test
        @DisplayName("무료 배송 기준이 없으면 항상 배송비를 반환한다")
        void shouldReturnDeliveryCostWhenNoThreshold() {
            // given
            ShippingPolicy policy = createPolicyWithoutFreeThreshold();

            // when
            int cost = policy.calculateDeliveryCost(1000000);

            // then
            assertEquals(5000, cost);
        }
    }

    @Nested
    @DisplayName("Law of Demeter Helper Methods")
    class HelperMethods {

        @Test
        @DisplayName("getIdValue()는 ID가 없으면 null을 반환한다")
        void shouldReturnNullWhenIdIsNull() {
            // given
            ShippingPolicy policy =
                    ShippingPolicy.create(
                            TEST_SELLER_ID,
                            PolicyName.of("테스트"),
                            DeliveryCost.of(3000),
                            null,
                            null,
                            true,
                            DisplayOrder.of(1),
                            FIXED_TIME);

            // then
            assertNull(policy.getIdValue());
            assertFalse(policy.hasId());
        }
    }

    // ========== Helper Methods ==========

    private ShippingPolicy createDefaultPolicy() {
        return ShippingPolicy.reconstitute(
                ShippingPolicyId.of(1L),
                TEST_SELLER_ID,
                PolicyName.of("기본 배송"),
                DeliveryCost.of(3000),
                FreeShippingThreshold.of(50000),
                DeliveryGuide.of("배송 안내"),
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

    private ShippingPolicy createPolicyWithoutFreeThreshold() {
        return ShippingPolicy.reconstitute(
                ShippingPolicyId.of(3L),
                TEST_SELLER_ID,
                PolicyName.of("무조건 유료"),
                DeliveryCost.of(5000),
                null,
                null,
                false,
                DisplayOrder.of(3),
                FIXED_TIME,
                FIXED_TIME,
                null);
    }
}
