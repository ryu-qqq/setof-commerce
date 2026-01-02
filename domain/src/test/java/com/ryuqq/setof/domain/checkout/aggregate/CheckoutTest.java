package com.ryuqq.setof.domain.checkout.aggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.checkout.exception.CheckoutStatusException;
import com.ryuqq.setof.domain.checkout.vo.CheckoutItem;
import com.ryuqq.setof.domain.checkout.vo.CheckoutMoney;
import com.ryuqq.setof.domain.checkout.vo.CheckoutStatus;
import com.ryuqq.setof.domain.checkout.vo.ShippingAddressSnapshot;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Checkout Aggregate")
class CheckoutTest {

    private static final Instant NOW = Instant.now();
    private static final int EXPIRATION_MINUTES = 30;

    @Nested
    @DisplayName("forNew() - 신규 결제 세션 생성")
    class ForNew {

        @Test
        @DisplayName("결제 세션을 생성할 수 있다")
        void shouldCreateCheckout() {
            // given
            String memberId = "member-001";
            List<CheckoutItem> items = createCheckoutItems();
            ShippingAddressSnapshot shippingAddress = createShippingAddress();
            CheckoutMoney discountAmount = CheckoutMoney.zero();

            // when
            Checkout checkout =
                    Checkout.forNew(
                            memberId,
                            items,
                            shippingAddress,
                            discountAmount,
                            EXPIRATION_MINUTES,
                            NOW);

            // then
            assertNotNull(checkout.id());
            assertEquals(memberId, checkout.memberId());
            assertEquals(CheckoutStatus.PENDING, checkout.status());
            assertEquals(1, checkout.items().size());
            assertNotNull(checkout.totalAmount());
            assertTrue(checkout.finalAmount().isPositive());
        }

        @Test
        @DisplayName("memberId가 null이면 예외가 발생한다")
        void shouldThrowExceptionWhenMemberIdIsNull() {
            // given
            String memberId = null;
            List<CheckoutItem> items = createCheckoutItems();
            ShippingAddressSnapshot shippingAddress = createShippingAddress();
            CheckoutMoney discountAmount = CheckoutMoney.zero();

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () ->
                            Checkout.forNew(
                                    memberId,
                                    items,
                                    shippingAddress,
                                    discountAmount,
                                    EXPIRATION_MINUTES,
                                    NOW));
        }
    }

    @Nested
    @DisplayName("상태 변경")
    class StatusTransition {

        @Test
        @DisplayName("PENDING → PROCESSING 상태 전이가 가능하다")
        void shouldTransitionFromPendingToProcessing() {
            // given
            Checkout checkout = createPendingCheckout();

            // when
            Checkout processing = checkout.startProcessing();

            // then
            assertEquals(CheckoutStatus.PROCESSING, processing.status());
        }

        @Test
        @DisplayName("PROCESSING → COMPLETED 상태 전이가 가능하다")
        void shouldTransitionFromProcessingToCompleted() {
            // given
            Checkout checkout = createPendingCheckout().startProcessing();
            Instant completedAt = Instant.now();

            // when
            Checkout completed = checkout.complete(completedAt);

            // then
            assertEquals(CheckoutStatus.COMPLETED, completed.status());
            assertNotNull(completed.completedAt());
        }

        @Test
        @DisplayName("PENDING → EXPIRED 상태 전이가 가능하다")
        void shouldTransitionFromPendingToExpired() {
            // given
            Checkout checkout = createPendingCheckout();

            // when
            Checkout expired = checkout.expire();

            // then
            assertEquals(CheckoutStatus.EXPIRED, expired.status());
        }

        @Test
        @DisplayName("COMPLETED 상태에서 PROCESSING으로 전이 시도 시 예외 발생")
        void shouldThrowExceptionWhenTransitionFromCompletedToProcessing() {
            // given
            Instant completedAt = Instant.now();
            Checkout completed = createPendingCheckout().startProcessing().complete(completedAt);

            // when & then
            assertThrows(CheckoutStatusException.class, completed::startProcessing);
        }
    }

    @Nested
    @DisplayName("금액 계산")
    class AmountCalculation {

        @Test
        @DisplayName("총 금액이 올바르게 계산된다")
        void shouldCalculateTotalAmount() {
            // given
            Checkout checkout = createPendingCheckout();

            // when
            CheckoutMoney totalAmount = checkout.totalAmount();

            // then
            assertTrue(totalAmount.isPositive());
        }

        @Test
        @DisplayName("최종 금액이 총 금액에서 할인을 뺀 값이다")
        void shouldCalculateFinalAmount() {
            // given
            String memberId = "member-001";
            List<CheckoutItem> items = createCheckoutItems();
            ShippingAddressSnapshot shippingAddress = createShippingAddress();
            CheckoutMoney discount = CheckoutMoney.of(BigDecimal.valueOf(1000));

            // when
            Checkout checkout =
                    Checkout.forNew(
                            memberId, items, shippingAddress, discount, EXPIRATION_MINUTES, NOW);

            // then
            CheckoutMoney expected = checkout.totalAmount().subtract(discount);
            assertEquals(expected, checkout.finalAmount());
        }
    }

    private Checkout createPendingCheckout() {
        String memberId = "member-001";
        List<CheckoutItem> items = createCheckoutItems();
        ShippingAddressSnapshot shippingAddress = createShippingAddress();
        CheckoutMoney discountAmount = CheckoutMoney.zero();
        return Checkout.forNew(
                memberId, items, shippingAddress, discountAmount, EXPIRATION_MINUTES, NOW);
    }

    private List<CheckoutItem> createCheckoutItems() {
        return List.of(
                CheckoutItem.of(
                        1L,
                        1L,
                        100L,
                        2,
                        CheckoutMoney.of(BigDecimal.valueOf(10000)),
                        "테스트 상품",
                        "https://img.url/test.jpg",
                        "옵션: 기본",
                        "테스트 브랜드",
                        "테스트 판매자"));
    }

    private ShippingAddressSnapshot createShippingAddress() {
        return ShippingAddressSnapshot.of(
                "홍길동", "01012345678", "12345", "서울시 강남구", "101동 101호", "문 앞");
    }
}
