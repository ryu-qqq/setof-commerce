package com.ryuqq.setof.domain.order.aggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.discount.vo.DiscountAmount;
import com.ryuqq.setof.domain.discount.vo.DiscountGroup;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyId;
import com.ryuqq.setof.domain.order.exception.OrderStatusException;
import com.ryuqq.setof.domain.order.vo.OrderDiscount;
import com.ryuqq.setof.domain.order.vo.OrderMoney;
import com.ryuqq.setof.domain.order.vo.OrderStatus;
import com.ryuqq.setof.domain.order.vo.ProductSnapshot;
import com.ryuqq.setof.domain.order.vo.ShippingInfo;
import com.ryuqq.setof.domain.payment.vo.PaymentId;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Order Aggregate")
class OrderTest {

    private static final Instant NOW = Instant.now();
    private static final String VALID_MEMBER_ID = "550e8400-e29b-41d4-a716-446655440000";

    @Nested
    @DisplayName("forNew() - 신규 주문 생성")
    class ForNew {

        @Test
        @DisplayName("주문을 생성할 수 있다")
        void shouldCreateOrder() {
            // given
            CheckoutId checkoutId = CheckoutId.forNew();
            PaymentId paymentId = PaymentId.forNew();
            String memberId = VALID_MEMBER_ID;
            Long sellerId = 100L;
            List<OrderItem> items = createOrderItems();
            ShippingInfo shippingInfo = createShippingInfo();
            OrderMoney shippingFee = OrderMoney.of(BigDecimal.valueOf(3000));

            // when
            Order order =
                    Order.forNew(
                            checkoutId,
                            paymentId,
                            sellerId,
                            memberId,
                            items,
                            shippingInfo,
                            shippingFee,
                            NOW);

            // then
            assertNotNull(order.id());
            assertNotNull(order.orderNumber());
            assertEquals(checkoutId, order.checkoutId());
            assertEquals(paymentId, order.paymentId());
            assertEquals(memberId, order.memberId());
            assertEquals(sellerId, order.sellerId());
            assertEquals(OrderStatus.PENDING, order.status());
            assertFalse(order.items().isEmpty());
        }

        @Test
        @DisplayName("memberId가 null이면 예외가 발생한다")
        void shouldThrowExceptionWhenMemberIdIsNull() {
            // given
            CheckoutId checkoutId = CheckoutId.forNew();
            PaymentId paymentId = PaymentId.forNew();
            Long sellerId = 100L;
            List<OrderItem> items = createOrderItems();
            ShippingInfo shippingInfo = createShippingInfo();
            OrderMoney shippingFee = OrderMoney.zero();

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () ->
                            Order.forNew(
                                    checkoutId,
                                    paymentId,
                                    sellerId,
                                    null,
                                    items,
                                    shippingInfo,
                                    shippingFee,
                                    NOW));
        }
    }

    @Nested
    @DisplayName("상태 전이")
    class StatusTransition {

        @Test
        @DisplayName("PENDING → CONFIRMED 상태 전이가 가능하다")
        void shouldTransitionFromPendingToConfirmed() {
            // given
            Order order = createPendingOrder();

            // when
            Order confirmed = order.confirm(NOW);

            // then
            assertEquals(OrderStatus.CONFIRMED, confirmed.status());
        }

        @Test
        @DisplayName("CONFIRMED → PREPARING 상태 전이가 가능하다")
        void shouldTransitionFromConfirmedToPreparing() {
            // given
            Order confirmed = createPendingOrder().confirm(NOW);

            // when
            Order preparing = confirmed.startPreparing(NOW);

            // then
            assertEquals(OrderStatus.PREPARING, preparing.status());
        }

        @Test
        @DisplayName("PREPARING → SHIPPED 상태 전이가 가능하다")
        void shouldTransitionFromPreparingToShipped() {
            // given
            Order preparing = createPendingOrder().confirm(NOW).startPreparing(NOW);

            // when
            Order shipped = preparing.ship(NOW);

            // then
            assertEquals(OrderStatus.SHIPPED, shipped.status());
        }

        @Test
        @DisplayName("SHIPPED → DELIVERED 상태 전이가 가능하다")
        void shouldTransitionFromShippedToDelivered() {
            // given
            Order shipped = createPendingOrder().confirm(NOW).startPreparing(NOW).ship(NOW);
            Instant deliveredAt = Instant.now();

            // when
            Order delivered = shipped.deliver(deliveredAt);

            // then
            assertEquals(OrderStatus.DELIVERED, delivered.status());
            assertNotNull(delivered.deliveredAt());
        }

        @Test
        @DisplayName("DELIVERED → COMPLETED 상태 전이가 가능하다")
        void shouldTransitionFromDeliveredToCompleted() {
            // given
            Order delivered =
                    createPendingOrder().confirm(NOW).startPreparing(NOW).ship(NOW).deliver(NOW);
            Instant completedAt = Instant.now();

            // when
            Order completed = delivered.complete(completedAt);

            // then
            assertEquals(OrderStatus.COMPLETED, completed.status());
            assertNotNull(completed.completedAt());
        }

        @Test
        @DisplayName("COMPLETED 상태에서 CONFIRMED로 전이 시 예외 발생")
        void shouldThrowExceptionWhenInvalidTransition() {
            // given
            Order completed =
                    createPendingOrder()
                            .confirm(NOW)
                            .startPreparing(NOW)
                            .ship(NOW)
                            .deliver(NOW)
                            .complete(NOW);

            // when & then
            assertThrows(OrderStatusException.class, () -> completed.confirm(NOW));
        }
    }

    @Nested
    @DisplayName("주문 취소")
    class Cancel {

        @Test
        @DisplayName("PENDING 상태의 주문을 취소할 수 있다")
        void shouldCancelPendingOrder() {
            // given
            Order order = createPendingOrder();
            Instant cancelledAt = Instant.now();

            // when
            Order cancelled = order.cancel(cancelledAt);

            // then
            assertEquals(OrderStatus.CANCELLED, cancelled.status());
            assertNotNull(cancelled.cancelledAt());
        }

        @Test
        @DisplayName("DELIVERED 상태의 주문은 취소할 수 없다")
        void shouldNotCancelDeliveredOrder() {
            // given
            Order delivered =
                    createPendingOrder().confirm(NOW).startPreparing(NOW).ship(NOW).deliver(NOW);

            // when & then
            assertThrows(OrderStatusException.class, () -> delivered.cancel(Instant.now()));
        }
    }

    @Nested
    @DisplayName("금액 계산")
    class AmountCalculation {

        @Test
        @DisplayName("총 금액이 올바르게 계산된다")
        void shouldCalculateTotalAmount() {
            // given
            Order order = createPendingOrder();

            // when
            OrderMoney totalAmount = order.totalAmount();

            // then
            assertTrue(totalAmount.isPositive());
        }

        @Test
        @DisplayName("총 상품 금액이 올바르게 계산된다")
        void shouldCalculateTotalItemAmount() {
            // given
            Order order = createPendingOrder();

            // when
            OrderMoney totalItemAmount = order.totalItemAmount();

            // then
            assertTrue(totalItemAmount.isPositive());
        }
    }

    @Nested
    @DisplayName("할인 적용")
    class DiscountApplication {

        @Test
        @DisplayName("할인을 포함한 주문을 생성할 수 있다")
        void shouldCreateOrderWithDiscount() {
            // given
            CheckoutId checkoutId = CheckoutId.forNew();
            PaymentId paymentId = PaymentId.forNew();
            List<OrderItem> items = createOrderItems();
            ShippingInfo shippingInfo = createShippingInfo();
            OrderMoney discountAmount = OrderMoney.of(5000L);
            List<OrderDiscount> discounts =
                    List.of(
                            OrderDiscount.of(
                                    DiscountPolicyId.of(1L),
                                    DiscountGroup.PRODUCT,
                                    DiscountAmount.of(5000L),
                                    "상품 할인"));

            // when
            Order order =
                    Order.forNew(
                            checkoutId,
                            paymentId,
                            100L,
                            VALID_MEMBER_ID,
                            items,
                            shippingInfo,
                            discountAmount,
                            discounts,
                            OrderMoney.zero(),
                            NOW);

            // then
            assertEquals(BigDecimal.valueOf(5000), order.discountAmountValue());
            assertEquals(1, order.discountCount());
            assertTrue(order.hasDiscount());
        }

        @Test
        @DisplayName("할인 없는 주문은 hasDiscount()가 false를 반환한다")
        void shouldReturnFalseForHasDiscountWhenNoDiscount() {
            // given
            Order order = createPendingOrder();

            // then
            assertFalse(order.hasDiscount());
            assertEquals(0, order.discountCount());
            assertEquals(BigDecimal.ZERO, order.discountAmountValue());
        }

        @Test
        @DisplayName("총 금액에서 할인이 차감된다")
        void shouldSubtractDiscountFromTotalAmount() {
            // given
            List<OrderItem> items = createOrderItems(); // 10000원 * 2개 = 20000원
            OrderMoney discountAmount = OrderMoney.of(5000L);
            List<OrderDiscount> discounts =
                    List.of(
                            OrderDiscount.of(
                                    DiscountPolicyId.of(1L),
                                    DiscountGroup.PRODUCT,
                                    DiscountAmount.of(5000L),
                                    "할인"));

            // when
            Order order =
                    Order.forNew(
                            CheckoutId.forNew(),
                            PaymentId.forNew(),
                            100L,
                            VALID_MEMBER_ID,
                            items,
                            createShippingInfo(),
                            discountAmount,
                            discounts,
                            OrderMoney.of(3000L),
                            NOW);

            // then - 총 상품금액(20000) - 할인(5000) + 배송비(3000) = 18000
            assertEquals(BigDecimal.valueOf(20000), order.totalItemAmount().value());
            assertEquals(BigDecimal.valueOf(5000), order.discountAmountValue());
            assertEquals(BigDecimal.valueOf(18000), order.totalAmount().value());
        }

        @Test
        @DisplayName("상태 전이 후에도 할인 정보가 유지된다")
        void shouldPreserveDiscountsAfterStateTransition() {
            // given
            OrderMoney discountAmount = OrderMoney.of(5000L);
            List<OrderDiscount> discounts =
                    List.of(
                            OrderDiscount.of(
                                    DiscountPolicyId.of(1L),
                                    DiscountGroup.PRODUCT,
                                    DiscountAmount.of(5000L),
                                    "할인"));
            Order order =
                    Order.forNew(
                            CheckoutId.forNew(),
                            PaymentId.forNew(),
                            100L,
                            VALID_MEMBER_ID,
                            createOrderItems(),
                            createShippingInfo(),
                            discountAmount,
                            discounts,
                            OrderMoney.zero(),
                            NOW);

            // when - 상태 전이
            Order confirmed = order.confirm(NOW);
            Order preparing = confirmed.startPreparing(NOW);
            Order shipped = preparing.ship(NOW);
            Order delivered = shipped.deliver(NOW);
            Order completed = delivered.complete(NOW);

            // then - 할인 정보가 유지됨
            assertEquals(BigDecimal.valueOf(5000), completed.discountAmountValue());
            assertEquals(1, completed.discountCount());
            assertTrue(completed.hasDiscount());
        }

        @Test
        @DisplayName("취소 후에도 할인 정보가 유지된다")
        void shouldPreserveDiscountsAfterCancellation() {
            // given
            OrderMoney discountAmount = OrderMoney.of(5000L);
            List<OrderDiscount> discounts =
                    List.of(
                            OrderDiscount.of(
                                    DiscountPolicyId.of(1L),
                                    DiscountGroup.MEMBER,
                                    DiscountAmount.of(5000L),
                                    "회원 할인"));
            Order order =
                    Order.forNew(
                            CheckoutId.forNew(),
                            PaymentId.forNew(),
                            100L,
                            VALID_MEMBER_ID,
                            createOrderItems(),
                            createShippingInfo(),
                            discountAmount,
                            discounts,
                            OrderMoney.zero(),
                            NOW);

            // when
            Order cancelled = order.cancel(NOW);

            // then
            assertEquals(OrderStatus.CANCELLED, cancelled.status());
            assertEquals(BigDecimal.valueOf(5000), cancelled.discountAmountValue());
            assertTrue(cancelled.hasDiscount());
        }
    }

    private Order createPendingOrder() {
        return Order.forNew(
                CheckoutId.forNew(),
                PaymentId.forNew(),
                100L,
                VALID_MEMBER_ID,
                createOrderItems(),
                createShippingInfo(),
                OrderMoney.zero(),
                NOW);
    }

    private List<OrderItem> createOrderItems() {
        ProductSnapshot snapshot =
                ProductSnapshot.of(
                        "상품A",
                        "https://img.url/a.jpg",
                        "옵션: 빨강/M",
                        "브랜드A",
                        "판매자A",
                        OrderMoney.of(BigDecimal.valueOf(10000)));
        return List.of(
                OrderItem.forNew(1L, 1L, 2, OrderMoney.of(BigDecimal.valueOf(10000)), snapshot));
    }

    private ShippingInfo createShippingInfo() {
        return ShippingInfo.of("홍길동", "01012345678", "12345", "서울시 강남구", "101동 101호", "문 앞");
    }
}
