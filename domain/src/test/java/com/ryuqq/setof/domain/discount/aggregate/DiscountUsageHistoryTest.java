package com.ryuqq.setof.domain.discount.aggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.discount.vo.CostShare;
import com.ryuqq.setof.domain.discount.vo.DiscountAmount;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyId;
import com.ryuqq.setof.domain.discount.vo.DiscountUsageHistoryId;
import com.ryuqq.setof.domain.order.vo.OrderId;
import com.ryuqq.setof.domain.order.vo.OrderMoney;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * DiscountUsageHistory Aggregate 테스트
 *
 * <p>할인 사용 히스토리 도메인 로직을 테스트합니다.
 */
@DisplayName("DiscountUsageHistory Aggregate")
class DiscountUsageHistoryTest {

    private static final DiscountPolicyId POLICY_ID = DiscountPolicyId.of(1L);
    private static final String MEMBER_ID = UUID.randomUUID().toString();
    private static final CheckoutId CHECKOUT_ID = CheckoutId.forNew();
    private static final OrderId ORDER_ID = OrderId.forNew();
    private static final DiscountAmount APPLIED_AMOUNT = DiscountAmount.of(10000L);
    private static final OrderMoney ORIGINAL_AMOUNT = OrderMoney.of(100000L);
    private static final CostShare COST_SHARE =
            CostShare.of(BigDecimal.valueOf(70), BigDecimal.valueOf(30));
    private static final Instant NOW = Instant.now();

    @Nested
    @DisplayName("신규 생성")
    class ForNew {

        @Test
        @DisplayName("신규 히스토리를 생성할 수 있다")
        void shouldCreateNewHistory() {
            // when
            DiscountUsageHistory history =
                    DiscountUsageHistory.forNew(
                            POLICY_ID,
                            MEMBER_ID,
                            CHECKOUT_ID,
                            ORDER_ID,
                            APPLIED_AMOUNT,
                            ORIGINAL_AMOUNT,
                            COST_SHARE,
                            NOW);

            // then
            assertNull(history.idValue()); // 신규 생성 시 ID는 null
            assertEquals(POLICY_ID.value(), history.discountPolicyIdValue());
            assertEquals(MEMBER_ID, history.memberId());
            assertEquals(CHECKOUT_ID.value().toString(), history.checkoutIdValue());
            assertEquals(ORDER_ID.value().toString(), history.orderIdValue());
            assertEquals(APPLIED_AMOUNT.value(), history.appliedAmountValue());
            assertEquals(ORIGINAL_AMOUNT.toLong(), history.originalAmountValue());
            assertNotNull(history.usedAt());
            assertNotNull(history.createdAt());
        }

        @Test
        @DisplayName("비용 분담 금액이 자동으로 계산된다")
        void shouldCalculateCostAutomatically() {
            // when
            DiscountUsageHistory history =
                    DiscountUsageHistory.forNew(
                            POLICY_ID,
                            MEMBER_ID,
                            CHECKOUT_ID,
                            ORDER_ID,
                            APPLIED_AMOUNT,
                            ORIGINAL_AMOUNT,
                            COST_SHARE,
                            NOW);

            // then - 10000원의 70%는 7000원, 30%는 3000원
            assertEquals(7000L, history.platformCost());
            assertEquals(3000L, history.sellerCost());
        }

        @Test
        @DisplayName("비용 분담 비율 스냅샷이 저장된다")
        void shouldStoreCostShareSnapshot() {
            // when
            DiscountUsageHistory history =
                    DiscountUsageHistory.forNew(
                            POLICY_ID,
                            MEMBER_ID,
                            CHECKOUT_ID,
                            ORDER_ID,
                            APPLIED_AMOUNT,
                            ORIGINAL_AMOUNT,
                            COST_SHARE,
                            NOW);

            // then
            assertEquals(new BigDecimal("70.00"), history.platformRatio());
            assertEquals(new BigDecimal("30.00"), history.sellerRatio());
        }
    }

    @Nested
    @DisplayName("복원")
    class FromPersistence {

        @Test
        @DisplayName("영속화된 데이터로부터 복원할 수 있다")
        void shouldRestoreFromPersistence() {
            // given
            DiscountUsageHistoryId id = DiscountUsageHistoryId.of(100L);
            long platformCost = 7000L;
            long sellerCost = 3000L;

            // when
            DiscountUsageHistory history =
                    DiscountUsageHistory.fromPersistence(
                            id,
                            POLICY_ID,
                            MEMBER_ID,
                            CHECKOUT_ID,
                            ORDER_ID,
                            APPLIED_AMOUNT,
                            ORIGINAL_AMOUNT,
                            COST_SHARE,
                            platformCost,
                            sellerCost,
                            NOW,
                            NOW);

            // then
            assertEquals(100L, history.idValue());
            assertEquals(platformCost, history.platformCost());
            assertEquals(sellerCost, history.sellerCost());
        }
    }

    @Nested
    @DisplayName("검증 실패")
    class ValidationFailure {

        @Test
        @DisplayName("정책 ID가 null이면 예외가 발생한다")
        void shouldThrowExceptionForNullPolicyId() {
            // when & then
            assertThrows(
                    NullPointerException.class,
                    () ->
                            DiscountUsageHistory.forNew(
                                    null,
                                    MEMBER_ID,
                                    CHECKOUT_ID,
                                    ORDER_ID,
                                    APPLIED_AMOUNT,
                                    ORIGINAL_AMOUNT,
                                    COST_SHARE,
                                    NOW));
        }

        @Test
        @DisplayName("회원 ID가 null이면 예외가 발생한다")
        void shouldThrowExceptionForNullMemberId() {
            // when & then
            assertThrows(
                    NullPointerException.class,
                    () ->
                            DiscountUsageHistory.forNew(
                                    POLICY_ID,
                                    null,
                                    CHECKOUT_ID,
                                    ORDER_ID,
                                    APPLIED_AMOUNT,
                                    ORIGINAL_AMOUNT,
                                    COST_SHARE,
                                    NOW));
        }

        @Test
        @DisplayName("Checkout ID가 null이면 예외가 발생한다")
        void shouldThrowExceptionForNullCheckoutId() {
            // when & then
            assertThrows(
                    NullPointerException.class,
                    () ->
                            DiscountUsageHistory.forNew(
                                    POLICY_ID,
                                    MEMBER_ID,
                                    null,
                                    ORDER_ID,
                                    APPLIED_AMOUNT,
                                    ORIGINAL_AMOUNT,
                                    COST_SHARE,
                                    NOW));
        }

        @Test
        @DisplayName("Order ID가 null이면 예외가 발생한다")
        void shouldThrowExceptionForNullOrderId() {
            // when & then
            assertThrows(
                    NullPointerException.class,
                    () ->
                            DiscountUsageHistory.forNew(
                                    POLICY_ID,
                                    MEMBER_ID,
                                    CHECKOUT_ID,
                                    null,
                                    APPLIED_AMOUNT,
                                    ORIGINAL_AMOUNT,
                                    COST_SHARE,
                                    NOW));
        }
    }

    @Nested
    @DisplayName("비즈니스 메서드")
    class BusinessMethods {

        @Test
        @DisplayName("할인 적용률을 계산할 수 있다")
        void shouldCalculateDiscountPercentage() {
            // given
            DiscountUsageHistory history =
                    DiscountUsageHistory.forNew(
                            POLICY_ID,
                            MEMBER_ID,
                            CHECKOUT_ID,
                            ORDER_ID,
                            APPLIED_AMOUNT,
                            ORIGINAL_AMOUNT,
                            COST_SHARE,
                            NOW);

            // when
            double percentage = history.discountPercentage();

            // then - 10000 / 100000 = 10%
            assertEquals(10.0, percentage, 0.01);
        }

        @Test
        @DisplayName("0원 할인인지 확인할 수 있다")
        void shouldCheckZeroDiscount() {
            // given
            DiscountUsageHistory zeroHistory =
                    DiscountUsageHistory.forNew(
                            POLICY_ID,
                            MEMBER_ID,
                            CHECKOUT_ID,
                            ORDER_ID,
                            DiscountAmount.zero(),
                            ORIGINAL_AMOUNT,
                            COST_SHARE,
                            NOW);

            DiscountUsageHistory nonZeroHistory =
                    DiscountUsageHistory.forNew(
                            POLICY_ID,
                            MEMBER_ID,
                            CHECKOUT_ID,
                            ORDER_ID,
                            APPLIED_AMOUNT,
                            ORIGINAL_AMOUNT,
                            COST_SHARE,
                            NOW);

            // then
            assertTrue(zeroHistory.isZeroDiscount());
            assertFalse(nonZeroHistory.isZeroDiscount());
        }

        @Test
        @DisplayName("원금이 0원일 때 할인율은 0%이다")
        void shouldReturnZeroPercentageForZeroOriginal() {
            // given
            DiscountUsageHistory history =
                    DiscountUsageHistory.forNew(
                            POLICY_ID,
                            MEMBER_ID,
                            CHECKOUT_ID,
                            ORDER_ID,
                            DiscountAmount.zero(),
                            OrderMoney.zero(),
                            COST_SHARE,
                            NOW);

            // when
            double percentage = history.discountPercentage();

            // then
            assertEquals(0.0, percentage, 0.01);
        }
    }

    @Nested
    @DisplayName("플랫폼 전액 부담")
    class PlatformOnly {

        @Test
        @DisplayName("플랫폼이 할인 비용을 전액 부담할 수 있다")
        void shouldPlatformPayAll() {
            // given
            CostShare platformOnly = CostShare.platformOnly();

            // when
            DiscountUsageHistory history =
                    DiscountUsageHistory.forNew(
                            POLICY_ID,
                            MEMBER_ID,
                            CHECKOUT_ID,
                            ORDER_ID,
                            APPLIED_AMOUNT,
                            ORIGINAL_AMOUNT,
                            platformOnly,
                            NOW);

            // then
            assertEquals(10000L, history.platformCost());
            assertEquals(0L, history.sellerCost());
        }
    }

    @Nested
    @DisplayName("셀러 전액 부담")
    class SellerOnly {

        @Test
        @DisplayName("셀러가 할인 비용을 전액 부담할 수 있다")
        void shouldSellerPayAll() {
            // given
            CostShare sellerOnly = CostShare.sellerOnly();

            // when
            DiscountUsageHistory history =
                    DiscountUsageHistory.forNew(
                            POLICY_ID,
                            MEMBER_ID,
                            CHECKOUT_ID,
                            ORDER_ID,
                            APPLIED_AMOUNT,
                            ORIGINAL_AMOUNT,
                            sellerOnly,
                            NOW);

            // then
            assertEquals(0L, history.platformCost());
            assertEquals(10000L, history.sellerCost());
        }
    }

    @Nested
    @DisplayName("동등성")
    class Equality {

        @Test
        @DisplayName("ID가 같은 히스토리는 동등하다")
        void shouldBeEqualForSameId() {
            // given
            DiscountUsageHistoryId id = DiscountUsageHistoryId.of(100L);

            DiscountUsageHistory history1 =
                    DiscountUsageHistory.fromPersistence(
                            id,
                            POLICY_ID,
                            MEMBER_ID,
                            CHECKOUT_ID,
                            ORDER_ID,
                            APPLIED_AMOUNT,
                            ORIGINAL_AMOUNT,
                            COST_SHARE,
                            7000L,
                            3000L,
                            NOW,
                            NOW);

            DiscountUsageHistory history2 =
                    DiscountUsageHistory.fromPersistence(
                            id,
                            POLICY_ID,
                            MEMBER_ID,
                            CHECKOUT_ID,
                            ORDER_ID,
                            APPLIED_AMOUNT,
                            ORIGINAL_AMOUNT,
                            COST_SHARE,
                            7000L,
                            3000L,
                            NOW,
                            NOW);

            // then
            assertEquals(history1, history2);
            assertEquals(history1.hashCode(), history2.hashCode());
        }
    }
}
