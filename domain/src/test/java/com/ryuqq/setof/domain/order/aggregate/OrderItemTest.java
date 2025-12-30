package com.ryuqq.setof.domain.order.aggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.order.exception.OrderItemQuantityException;
import com.ryuqq.setof.domain.order.vo.OrderItemStatus;
import com.ryuqq.setof.domain.order.vo.OrderMoney;
import com.ryuqq.setof.domain.order.vo.ProductSnapshot;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("OrderItem Entity")
class OrderItemTest {

    @Nested
    @DisplayName("forNew() - 신규 주문 항목 생성")
    class ForNew {

        @Test
        @DisplayName("주문 항목을 생성할 수 있다")
        void shouldCreateOrderItem() {
            // given
            Long productId = 1L;
            Long productStockId = 1L;
            int quantity = 3;
            OrderMoney unitPrice = OrderMoney.of(BigDecimal.valueOf(15000));
            ProductSnapshot snapshot = createSnapshot();

            // when
            OrderItem item =
                    OrderItem.forNew(productId, productStockId, quantity, unitPrice, snapshot);

            // then
            assertNotNull(item.id());
            assertEquals(productId, item.productId());
            assertEquals(productStockId, item.productStockId());
            assertEquals(quantity, item.orderedQuantity());
            assertEquals(0, item.cancelledQuantity());
            assertEquals(0, item.refundedQuantity());
            assertEquals(unitPrice, item.unitPrice());
            assertEquals(OrderItemStatus.defaultStatus(), item.status());
        }

        @Test
        @DisplayName("총 금액이 올바르게 계산된다")
        void shouldCalculateTotalPrice() {
            // given
            int quantity = 3;
            OrderMoney unitPrice = OrderMoney.of(BigDecimal.valueOf(15000));

            // when
            OrderItem item = OrderItem.forNew(1L, 1L, quantity, unitPrice, createSnapshot());

            // then
            OrderMoney expectedTotal = unitPrice.multiply(quantity);
            assertEquals(expectedTotal, item.totalPrice());
        }
    }

    @Nested
    @DisplayName("취소")
    class Cancel {

        @Test
        @DisplayName("전체 수량을 취소할 수 있다")
        void shouldCancelFullQuantity() {
            // given
            OrderItem item = createOrderItem(3);

            // when
            OrderItem cancelled = item.cancel(3);

            // then
            assertEquals(3, cancelled.cancelledQuantity());
            assertEquals(0, cancelled.effectiveQuantity());
            assertEquals(OrderItemStatus.CANCELLED, cancelled.status());
        }

        @Test
        @DisplayName("부분 수량을 취소할 수 있다")
        void shouldCancelPartialQuantity() {
            // given
            OrderItem item = createOrderItem(3);

            // when
            OrderItem cancelled = item.cancel(1);

            // then
            assertEquals(1, cancelled.cancelledQuantity());
            assertEquals(2, cancelled.effectiveQuantity());
        }

        @Test
        @DisplayName("가용 수량을 초과하면 예외가 발생한다")
        void shouldThrowExceptionWhenCancelExceedsAvailable() {
            // given
            OrderItem item = createOrderItem(3);

            // when & then
            assertThrows(OrderItemQuantityException.class, () -> item.cancel(5));
        }
    }

    @Nested
    @DisplayName("환불")
    class Refund {

        @Test
        @DisplayName("전체 수량을 환불할 수 있다")
        void shouldRefundFullQuantity() {
            // given
            OrderItem item = createOrderItem(3);

            // when
            OrderItem refunded = item.refund(3);

            // then
            assertEquals(3, refunded.refundedQuantity());
            assertEquals(0, refunded.effectiveQuantity());
        }

        @Test
        @DisplayName("부분 수량을 환불할 수 있다")
        void shouldRefundPartialQuantity() {
            // given
            OrderItem item = createOrderItem(3);

            // when
            OrderItem refunded = item.refund(1);

            // then
            assertEquals(1, refunded.refundedQuantity());
            assertEquals(2, refunded.effectiveQuantity());
        }

        @Test
        @DisplayName("가용 수량을 초과하면 예외가 발생한다")
        void shouldThrowExceptionWhenRefundExceedsAvailable() {
            // given
            OrderItem item = createOrderItem(3);

            // when & then
            assertThrows(OrderItemQuantityException.class, () -> item.refund(5));
        }

        @Test
        @DisplayName("취소 후 남은 수량만 환불 가능하다")
        void shouldOnlyRefundRemainingAfterCancel() {
            // given
            OrderItem item = createOrderItem(3).cancel(2);

            // when
            OrderItem refunded = item.refund(1);

            // then
            assertEquals(2, refunded.cancelledQuantity());
            assertEquals(1, refunded.refundedQuantity());
            assertEquals(0, refunded.effectiveQuantity());
        }
    }

    @Nested
    @DisplayName("금액 계산")
    class AmountCalculation {

        @Test
        @DisplayName("유효 수량이 올바르게 계산된다")
        void shouldCalculateEffectiveQuantity() {
            // given
            OrderItem item = createOrderItem(5);
            OrderItem cancelled = item.cancel(2);
            OrderItem refunded = cancelled.refund(1);

            // when
            int effectiveQuantity = refunded.effectiveQuantity();

            // then
            assertEquals(2, effectiveQuantity);
        }

        @Test
        @DisplayName("유효 금액이 올바르게 계산된다")
        void shouldCalculateEffectiveAmount() {
            // given
            OrderMoney unitPrice = OrderMoney.of(BigDecimal.valueOf(10000));
            OrderItem item = OrderItem.forNew(1L, 1L, 5, unitPrice, createSnapshot());
            OrderItem cancelled = item.cancel(2);

            // when
            OrderMoney effectiveAmount = cancelled.effectiveAmount();

            // then
            OrderMoney expected = unitPrice.multiply(3);
            assertEquals(expected, effectiveAmount);
        }

        @Test
        @DisplayName("환불 가능 금액이 올바르게 계산된다")
        void shouldCalculateRefundableAmount() {
            // given
            OrderItem item = createOrderItem(5).cancel(2);

            // when
            OrderMoney refundableAmount = item.refundableAmount();

            // then
            assertEquals(item.effectiveAmount(), refundableAmount);
        }
    }

    @Nested
    @DisplayName("완전 취소/환불 여부")
    class FullyCancelledOrRefunded {

        @Test
        @DisplayName("전체 취소 시 true를 반환한다")
        void shouldReturnTrueWhenFullyCancelled() {
            // given
            OrderItem item = createOrderItem(3).cancel(3);

            // when & then
            assertTrue(item.isFullyCancelledOrRefunded());
        }

        @Test
        @DisplayName("전체 환불 시 true를 반환한다")
        void shouldReturnTrueWhenFullyRefunded() {
            // given
            OrderItem item = createOrderItem(3).refund(3);

            // when & then
            assertTrue(item.isFullyCancelledOrRefunded());
        }

        @Test
        @DisplayName("취소+환불로 전체 처리 시 true를 반환한다")
        void shouldReturnTrueWhenFullyCancelledAndRefunded() {
            // given
            OrderItem item = createOrderItem(3).cancel(2).refund(1);

            // when & then
            assertTrue(item.isFullyCancelledOrRefunded());
        }

        @Test
        @DisplayName("부분 취소 시 false를 반환한다")
        void shouldReturnFalseWhenPartialCancelled() {
            // given
            OrderItem item = createOrderItem(3).cancel(1);

            // when & then
            assertFalse(item.isFullyCancelledOrRefunded());
        }
    }

    private OrderItem createOrderItem(int quantity) {
        return OrderItem.forNew(
                1L, 1L, quantity, OrderMoney.of(BigDecimal.valueOf(10000)), createSnapshot());
    }

    private ProductSnapshot createSnapshot() {
        return ProductSnapshot.of(
                "상품A",
                "https://img.url/a.jpg",
                "옵션: 빨강/M",
                "브랜드A",
                "판매자A",
                OrderMoney.of(BigDecimal.valueOf(10000)));
    }
}
