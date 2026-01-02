package com.ryuqq.setof.domain.order.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.discount.vo.DiscountAmount;
import com.ryuqq.setof.domain.discount.vo.DiscountGroup;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyId;
import com.ryuqq.setof.domain.order.exception.InvalidOrderDiscountException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * OrderDiscount Value Object 테스트
 *
 * <p>주문 할인 정보 스냅샷 VO의 생성 및 검증 로직을 테스트합니다.
 */
@DisplayName("OrderDiscount VO")
class OrderDiscountTest {

    private static final DiscountPolicyId POLICY_ID = DiscountPolicyId.of(1L);
    private static final DiscountGroup DISCOUNT_GROUP = DiscountGroup.PRODUCT;
    private static final DiscountAmount AMOUNT = DiscountAmount.of(10000L);
    private static final String POLICY_NAME = "2024 신년 할인";

    @Nested
    @DisplayName("생성")
    class Creation {

        @Test
        @DisplayName("유효한 정보로 생성할 수 있다")
        void shouldCreateWithValidData() {
            // when
            OrderDiscount discount =
                    OrderDiscount.of(POLICY_ID, DISCOUNT_GROUP, AMOUNT, POLICY_NAME);

            // then
            assertEquals(POLICY_ID, discount.policyId());
            assertEquals(DISCOUNT_GROUP, discount.discountGroup());
            assertEquals(AMOUNT, discount.amount());
            assertEquals(POLICY_NAME, discount.policyName());
        }

        @Test
        @DisplayName("모든 할인 그룹으로 생성할 수 있다")
        void shouldCreateWithDifferentGroups() {
            for (DiscountGroup group : DiscountGroup.values()) {
                // when
                OrderDiscount discount = OrderDiscount.of(POLICY_ID, group, AMOUNT, POLICY_NAME);

                // then
                assertEquals(group, discount.discountGroup());
            }
        }
    }

    @Nested
    @DisplayName("검증 실패")
    class ValidationFailure {

        @Test
        @DisplayName("policyId가 null이면 예외가 발생한다")
        void shouldThrowExceptionForNullPolicyId() {
            // when & then
            assertThrows(
                    InvalidOrderDiscountException.class,
                    () -> OrderDiscount.of(null, DISCOUNT_GROUP, AMOUNT, POLICY_NAME));
        }

        @Test
        @DisplayName("discountGroup이 null이면 예외가 발생한다")
        void shouldThrowExceptionForNullDiscountGroup() {
            // when & then
            assertThrows(
                    InvalidOrderDiscountException.class,
                    () -> OrderDiscount.of(POLICY_ID, null, AMOUNT, POLICY_NAME));
        }

        @Test
        @DisplayName("amount가 null이면 예외가 발생한다")
        void shouldThrowExceptionForNullAmount() {
            // when & then
            assertThrows(
                    InvalidOrderDiscountException.class,
                    () -> OrderDiscount.of(POLICY_ID, DISCOUNT_GROUP, null, POLICY_NAME));
        }

        @Test
        @DisplayName("policyName이 null이면 예외가 발생한다")
        void shouldThrowExceptionForNullPolicyName() {
            // when & then
            assertThrows(
                    InvalidOrderDiscountException.class,
                    () -> OrderDiscount.of(POLICY_ID, DISCOUNT_GROUP, AMOUNT, null));
        }

        @Test
        @DisplayName("policyName이 빈 문자열이면 예외가 발생한다")
        void shouldThrowExceptionForEmptyPolicyName() {
            // when & then
            assertThrows(
                    InvalidOrderDiscountException.class,
                    () -> OrderDiscount.of(POLICY_ID, DISCOUNT_GROUP, AMOUNT, ""));
        }

        @Test
        @DisplayName("policyName이 공백만 있으면 예외가 발생한다")
        void shouldThrowExceptionForBlankPolicyName() {
            // when & then
            assertThrows(
                    InvalidOrderDiscountException.class,
                    () -> OrderDiscount.of(POLICY_ID, DISCOUNT_GROUP, AMOUNT, "   "));
        }
    }

    @Nested
    @DisplayName("Helper Methods (Law of Demeter)")
    class HelperMethods {

        @Test
        @DisplayName("policyIdValue()로 정책 ID 값을 반환한다")
        void shouldReturnPolicyIdValue() {
            // given
            OrderDiscount discount =
                    OrderDiscount.of(POLICY_ID, DISCOUNT_GROUP, AMOUNT, POLICY_NAME);

            // when & then
            assertEquals(1L, discount.policyIdValue());
        }

        @Test
        @DisplayName("amountValue()로 할인 금액 값을 반환한다")
        void shouldReturnAmountValue() {
            // given
            OrderDiscount discount =
                    OrderDiscount.of(POLICY_ID, DISCOUNT_GROUP, AMOUNT, POLICY_NAME);

            // when & then
            assertEquals(10000L, discount.amountValue());
        }

        @Test
        @DisplayName("discountGroupDescription()으로 할인 그룹 설명을 반환한다")
        void shouldReturnDiscountGroupDescription() {
            // given
            OrderDiscount discount =
                    OrderDiscount.of(POLICY_ID, DiscountGroup.PRODUCT, AMOUNT, POLICY_NAME);

            // when & then
            assertEquals("상품 할인", discount.discountGroupDescription());
        }

        @Test
        @DisplayName("isZeroDiscount()로 0원 할인 여부를 확인할 수 있다")
        void shouldCheckZeroDiscount() {
            // given
            OrderDiscount zeroDiscount =
                    OrderDiscount.of(POLICY_ID, DISCOUNT_GROUP, DiscountAmount.zero(), POLICY_NAME);
            OrderDiscount nonZeroDiscount =
                    OrderDiscount.of(POLICY_ID, DISCOUNT_GROUP, AMOUNT, POLICY_NAME);

            // when & then
            assertTrue(zeroDiscount.isZeroDiscount());
            assertFalse(nonZeroDiscount.isZeroDiscount());
        }
    }

    @Nested
    @DisplayName("동등성")
    class Equality {

        @Test
        @DisplayName("같은 값을 가진 OrderDiscount는 동등하다")
        void shouldBeEqualForSameValues() {
            // given
            OrderDiscount discount1 =
                    OrderDiscount.of(POLICY_ID, DISCOUNT_GROUP, AMOUNT, POLICY_NAME);
            OrderDiscount discount2 =
                    OrderDiscount.of(POLICY_ID, DISCOUNT_GROUP, AMOUNT, POLICY_NAME);

            // then
            assertEquals(discount1, discount2);
            assertEquals(discount1.hashCode(), discount2.hashCode());
        }
    }

    @Nested
    @DisplayName("비즈니스 시나리오")
    class BusinessScenarios {

        @Test
        @DisplayName("PRODUCT 그룹 할인 생성")
        void shouldCreateProductDiscount() {
            // when
            OrderDiscount discount =
                    OrderDiscount.of(
                            DiscountPolicyId.of(100L),
                            DiscountGroup.PRODUCT,
                            DiscountAmount.of(5000L),
                            "상품 특가 할인");

            // then
            assertEquals(100L, discount.policyIdValue());
            assertEquals(DiscountGroup.PRODUCT, discount.discountGroup());
            assertEquals(5000L, discount.amountValue());
        }

        @Test
        @DisplayName("MEMBER 그룹 할인 생성")
        void shouldCreateMemberDiscount() {
            // when
            OrderDiscount discount =
                    OrderDiscount.of(
                            DiscountPolicyId.of(200L),
                            DiscountGroup.MEMBER,
                            DiscountAmount.of(3000L),
                            "VIP 회원 할인");

            // then
            assertEquals(200L, discount.policyIdValue());
            assertEquals(DiscountGroup.MEMBER, discount.discountGroup());
            assertEquals("VIP 회원 할인", discount.policyName());
        }

        @Test
        @DisplayName("PAYMENT 그룹 할인 생성")
        void shouldCreatePaymentDiscount() {
            // when
            OrderDiscount discount =
                    OrderDiscount.of(
                            DiscountPolicyId.of(300L),
                            DiscountGroup.PAYMENT,
                            DiscountAmount.of(2000L),
                            "카드 결제 할인");

            // then
            assertEquals(300L, discount.policyIdValue());
            assertEquals(DiscountGroup.PAYMENT, discount.discountGroup());
            assertEquals("결제 할인", discount.discountGroupDescription());
        }
    }
}
