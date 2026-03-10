package com.ryuqq.setof.domain.order.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.order.exception.InvalidOrderStatusTransitionException;
import com.ryuqq.setof.domain.order.vo.OrderStatus;
import com.setof.commerce.domain.order.OrderFixtures;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("Order Aggregate 단위 테스트")
class OrderTest {

    @Nested
    @DisplayName("forNew() - 신규 주문 생성")
    class ForNewTest {

        @Test
        @DisplayName("필수 정보로 신규 주문을 PENDING 상태로 생성한다")
        void createNewOrderWithPendingStatus() {
            // given
            Instant now = CommonVoFixtures.now();

            // when
            Order order =
                    Order.forNew(
                            OrderFixtures.defaultMemberId(),
                            OrderFixtures.defaultLegacyUserId(),
                            OrderFixtures.defaultReceiverInfo(),
                            List.of(OrderFixtures.newOrderItem()),
                            now);

            // then
            assertThat(order.isNew()).isTrue();
            assertThat(order.orderStatus()).isEqualTo(OrderStatus.PENDING);
            assertThat(order.isPending()).isTrue();
            assertThat(order.isConfirmed()).isFalse();
            assertThat(order.isFinal()).isFalse();
            assertThat(order.orderItems()).hasSize(1);
            assertThat(order.createdAt()).isEqualTo(now);
            assertThat(order.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("주문 아이템이 비어있으면 예외가 발생한다")
        void createNewOrderWithEmptyItems_ThrowsException() {
            // when & then
            assertThatThrownBy(
                            () ->
                                    Order.forNew(
                                            OrderFixtures.defaultMemberId(),
                                            OrderFixtures.defaultLegacyUserId(),
                                            OrderFixtures.defaultReceiverInfo(),
                                            List.of(),
                                            CommonVoFixtures.now()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("주문 아이템은 최소 1개 이상이어야 합니다");
        }

        @Test
        @DisplayName("주문 아이템 목록이 null이면 예외가 발생한다")
        void createNewOrderWithNullItems_ThrowsException() {
            // when & then
            assertThatThrownBy(
                            () ->
                                    Order.forNew(
                                            OrderFixtures.defaultMemberId(),
                                            OrderFixtures.defaultLegacyUserId(),
                                            OrderFixtures.defaultReceiverInfo(),
                                            null,
                                            CommonVoFixtures.now()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("회원 ID 없이 주문을 생성할 수 있다 (레거시 마이그레이션 허용)")
        void createNewOrderWithoutMemberId() {
            // when
            Order order =
                    Order.forNew(
                            null,
                            OrderFixtures.defaultLegacyUserId(),
                            OrderFixtures.defaultReceiverInfo(),
                            List.of(OrderFixtures.newOrderItem()),
                            CommonVoFixtures.now());

            // then
            assertThat(order.memberId()).isNull();
            assertThat(order.memberIdValue()).isNull();
        }

        @Test
        @DisplayName("주문 아이템 목록이 불변으로 관리된다")
        void orderItemsAreImmutable() {
            // when
            Order order = OrderFixtures.newOrder();

            // then
            assertThatThrownBy(() -> order.orderItems().add(OrderFixtures.newOrderItem()))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성에서 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("영속성에서 PLACED 상태의 주문을 복원한다")
        void reconstitutePlacedOrder() {
            // when
            Order order = OrderFixtures.placedOrder();

            // then
            assertThat(order.isNew()).isFalse();
            assertThat(order.orderStatus()).isEqualTo(OrderStatus.PLACED);
            assertThat(order.isConfirmed()).isTrue();
        }

        @Test
        @DisplayName("아이템 목록이 null이면 빈 리스트로 복원된다")
        void reconstituteWithNullItems() {
            // when
            Order order =
                    Order.reconstitute(
                            OrderFixtures.defaultOrderId(),
                            OrderFixtures.defaultMemberId(),
                            OrderFixtures.defaultLegacyUserId(),
                            OrderFixtures.defaultReceiverInfo(),
                            null,
                            OrderStatus.PENDING,
                            CommonVoFixtures.yesterday(),
                            CommonVoFixtures.yesterday());

            // then
            assertThat(order.orderItems()).isEmpty();
        }
    }

    @Nested
    @DisplayName("상태 전이 메서드 테스트")
    class StatusTransitionTest {

        @Test
        @DisplayName("PENDING 상태에서 place()를 호출하면 PLACED로 전이한다")
        void placeOrderFromPending() {
            // given
            Order order = OrderFixtures.pendingOrder();
            Instant now = CommonVoFixtures.now();

            // when
            order.place(now);

            // then
            assertThat(order.orderStatus()).isEqualTo(OrderStatus.PLACED);
            assertThat(order.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("PENDING 상태에서 fail()을 호출하면 FAILED로 전이한다")
        void failOrderFromPending() {
            // given
            Order order = OrderFixtures.pendingOrder();
            Instant now = CommonVoFixtures.now();

            // when
            order.fail(now);

            // then
            assertThat(order.orderStatus()).isEqualTo(OrderStatus.FAILED);
            assertThat(order.isFinal()).isTrue();
        }

        @Test
        @DisplayName("PENDING 상태에서 expire()를 호출하면 EXPIRED로 전이한다")
        void expireOrderFromPending() {
            // given
            Order order = OrderFixtures.pendingOrder();
            Instant now = CommonVoFixtures.now();

            // when
            order.expire(now);

            // then
            assertThat(order.orderStatus()).isEqualTo(OrderStatus.EXPIRED);
            assertThat(order.isFinal()).isTrue();
        }

        @Test
        @DisplayName("PLACED 상태에서 confirm()을 호출하면 CONFIRMED로 전이한다")
        void confirmOrderFromPlaced() {
            // given
            Order order = OrderFixtures.placedOrder();
            Instant now = CommonVoFixtures.now();

            // when
            order.confirm(now);

            // then
            assertThat(order.orderStatus()).isEqualTo(OrderStatus.CONFIRMED);
        }

        @Test
        @DisplayName("PLACED 상태에서 cancel()을 호출하면 CANCELLED로 전이한다")
        void cancelOrderFromPlaced() {
            // given
            Order order = OrderFixtures.placedOrder();
            Instant now = CommonVoFixtures.now();

            // when
            order.cancel(now);

            // then
            assertThat(order.orderStatus()).isEqualTo(OrderStatus.CANCELLED);
            assertThat(order.isFinal()).isTrue();
        }

        @Test
        @DisplayName("CONFIRMED 상태에서 complete()를 호출하면 COMPLETED로 전이한다")
        void completeOrderFromConfirmed() {
            // given
            Order order = OrderFixtures.confirmedOrder();
            Instant now = CommonVoFixtures.now();

            // when
            order.complete(now);

            // then
            assertThat(order.orderStatus()).isEqualTo(OrderStatus.COMPLETED);
            assertThat(order.isFinal()).isTrue();
        }
    }

    @Nested
    @DisplayName("허용되지 않는 상태 전이 테스트")
    class InvalidTransitionTest {

        @Test
        @DisplayName("FAILED 상태에서 place()를 호출하면 예외가 발생한다")
        void placeOrderFromFailed_ThrowsException() {
            // given
            Order order = OrderFixtures.failedOrder();

            // when & then
            assertThatThrownBy(() -> order.place(CommonVoFixtures.now()))
                    .isInstanceOf(InvalidOrderStatusTransitionException.class);
        }

        @Test
        @DisplayName("PENDING 상태에서 confirm()을 호출하면 예외가 발생한다")
        void confirmOrderFromPending_ThrowsException() {
            // given
            Order order = OrderFixtures.pendingOrder();

            // when & then
            assertThatThrownBy(() -> order.confirm(CommonVoFixtures.now()))
                    .isInstanceOf(InvalidOrderStatusTransitionException.class);
        }
    }

    @Nested
    @DisplayName("assignPaymentId() - 결제 ID 할당")
    class AssignPaymentIdTest {

        @Test
        @DisplayName("양수의 결제 ID를 할당한다")
        void assignPositivePaymentId() {
            // given
            Order order = OrderFixtures.pendingOrder();

            // when
            order.assignPaymentId(42L);

            // then
            assertThat(order.paymentId()).isEqualTo(42L);
        }

        @Test
        @DisplayName("결제 ID가 0이면 예외가 발생한다")
        void assignZeroPaymentId_ThrowsException() {
            // given
            Order order = OrderFixtures.pendingOrder();

            // when & then
            assertThatThrownBy(() -> order.assignPaymentId(0L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("paymentId must be positive");
        }

        @Test
        @DisplayName("결제 ID가 음수이면 예외가 발생한다")
        void assignNegativePaymentId_ThrowsException() {
            // given
            Order order = OrderFixtures.pendingOrder();

            // when & then
            assertThatThrownBy(() -> order.assignPaymentId(-1L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("paymentId must be positive");
        }
    }

    @Nested
    @DisplayName("totalOrderAmount() - 총 주문 금액 계산")
    class TotalOrderAmountTest {

        @Test
        @DisplayName("단일 아이템 주문의 총 금액을 반환한다")
        void totalAmountForSingleItemOrder() {
            // given
            Order order = OrderFixtures.pendingOrder();

            // when
            int total = order.totalOrderAmount();

            // then
            assertThat(total).isEqualTo(OrderFixtures.defaultOrderItemPrice().orderAmount());
        }

        @Test
        @DisplayName("복수 아이템 주문의 총 금액을 합산한다")
        void totalAmountForMultipleItemOrder() {
            // given
            Order order = OrderFixtures.newOrderWithMultipleItems();

            // when
            int total = order.totalOrderAmount();

            // then
            assertThat(total).isGreaterThan(0);
        }
    }
}
